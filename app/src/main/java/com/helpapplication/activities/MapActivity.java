package com.helpapplication.activities;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.helpapplication.R;
import com.helpapplication.events.UploadCompletedEvent;
import com.helpapplication.events.UploadStartEvent;
import com.helpapplication.service.MapTrackingService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mGoogleMap;
    private String TAG = MapActivity.class.getSimpleName();

    private ProgressDialog mUploadDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        checkForEnvironment();
        initView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        dismissUploadProgressDialog();
    }

    public void checkForEnvironment() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if (manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState()
                != NetworkInfo.State.CONNECTED && manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState()
                != NetworkInfo.State.CONNECTED) {
            Toast.makeText(this, "인터넷 연결 후 사용해주세요.", Toast.LENGTH_SHORT).show();
            finish();
        }

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            Toast.makeText(this, "GPS를 키고 사용해주세요.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void initView() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.main_map_container);
        mapFragment.getMapAsync(this);

        final Intent serviceIntent = new Intent(MapActivity.this, MapTrackingService.class);

        findViewById(R.id.start_txt_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(serviceIntent);
            }
        });

        findViewById(R.id.end_txt_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isServiceRunningCheck()) {
                    stopService(serviceIntent);
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        LocationManager locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        if (location != null) {
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 18.0f);
            mGoogleMap.moveCamera(cameraUpdate);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveLocations(List<LatLng> locationsForGoogleMap) {
        Log.d(TAG, "onReceiveLocations: " + locationsForGoogleMap);
        mGoogleMap.clear();
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.color(Color.parseColor("#2196f3"));
        polylineOptions.addAll(locationsForGoogleMap);

        LatLng lastLatLng = locationsForGoogleMap.get(locationsForGoogleMap.size() - 1);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(lastLatLng.latitude, lastLatLng.longitude), 18.0f);

        mGoogleMap.addPolyline(polylineOptions);
        mGoogleMap.moveCamera(cameraUpdate);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveUploadStarted(UploadStartEvent event) {
        showUploadProgressDialog();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveUploadCompleted(UploadCompletedEvent event) {
        dismissUploadProgressDialog();

        if (event.isSuccessful()) {
            Toast.makeText(this, "업로드가 정상적으로 완료되었습니다!", Toast.LENGTH_SHORT).show();
            return;
        }

        int responseCode = event.getCode();

        switch (responseCode) {
            case 400:
                Toast.makeText(this, event.getErrorMessage(), Toast.LENGTH_SHORT).show();
                break;
            case 500:
                Toast.makeText(this, "서버에서 오류가 발생했습니다!", Toast.LENGTH_SHORT).show();
                break;
        }

    }

    private void showUploadProgressDialog() {
        if (mUploadDialog == null) {
            mUploadDialog = new ProgressDialog(MapActivity.this);
            mUploadDialog.setCancelable(false);
            mUploadDialog.setTitle("업로드 중");
            mUploadDialog.setMessage("데이터를 서버에 업로드 하고 있습니다!");
        }

        mUploadDialog.show();
    }

    private void dismissUploadProgressDialog() {
        if (mUploadDialog != null) {
            mUploadDialog.dismiss();
        }
    }


    public boolean isServiceRunningCheck() {
        ActivityManager manager = (ActivityManager) this.getSystemService(Activity.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("com.helpapplication.service.MapTrackingService".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
