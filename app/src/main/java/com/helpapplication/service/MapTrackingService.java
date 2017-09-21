package com.helpapplication.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.helpapplication.R;
import com.helpapplication.events.UploadStartEvent;
import com.helpapplication.http.model.Info;
import com.helpapplication.utils.AlertReceiver;
import com.helpapplication.utils.LocationReceiver;
import com.helpapplication.utils.PreferenceManager;
import com.helpapplication.utils.Recorder;
import com.helpapplication.utils.SmsSender;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by donghaechoi on 2017. 2. 24..
 */

public class MapTrackingService extends Service {

    private PreferenceManager mPreferenceManager;
    private LocationReceiver mLocationReceiver;
    private SmsSender mSmsSender;
    private Recorder mRecorder;
    private AlertReceiver mAlertReceiver;
    private Timer mSmsSendingTimer;
    private boolean mIsStarted = false;

    @Override
    public void onCreate() {
        super.onCreate();
        mPreferenceManager = PreferenceManager.getInstance(getApplicationContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!mIsStarted) {
            serviceFunction();
        }
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private LocationReceiver.LocationCallback mLocationCallback = new LocationReceiver.LocationCallback() {
        @Override
        public void onLocationReceived(Location location) {

        }

        @Override
        public void onLocationAppended(List<LatLng> locationForGoogleMap) {
            EventBus.getDefault().post(locationForGoogleMap);
        }
    };

    private AlertReceiver.Callback mAlertCallback = new AlertReceiver.Callback() {
        @Override
        public void onAlertReceived() {
            mSmsSender.sendAlertSmsMessage();

            if (!mRecorder.isRecording()) {
                mRecorder.startRecord();
            }
        }
    };


    public void serviceFunction() {
        if (mPreferenceManager.isServiceUnUses()) {
            return;
        }

        mRecorder = Recorder.getInstance();
        mSmsSender = SmsSender.getInstance(getApplicationContext());
        mAlertReceiver = AlertReceiver.getInstance(getApplicationContext(), mAlertCallback);
        mAlertReceiver.start();
        mLocationReceiver = LocationReceiver.getInstance(getApplicationContext(), mLocationCallback);
        mLocationReceiver.requestLocation();

        Notification.Builder notification = new Notification.Builder(getApplicationContext());
        notification.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("서비스 실행중")
                .setContentText("서비스 실행중")
                .setSubText("서비스가 실행중입니다.");
        startForeground(1, notification.build());

        mIsStarted = true;

        long smsSendingPeriod = 1000;

        if (mPreferenceManager.isServicePeriodOneMinute()) {
            smsSendingPeriod = 60 * 1000;
        } else if (mPreferenceManager.isServicePeriodThreeMinute()) {
            smsSendingPeriod = 3 * 60 * 1000;
        } else if (mPreferenceManager.isServicePeriodFiveMinute()) {
            smsSendingPeriod = 5 * 60 * 1000;
        }

        mSmsSendingTimer = new Timer();
        mSmsSendingTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Location myLocation = mLocationReceiver.getCurrentLocation();
                mSmsSender.sendLocationSmsMessage(myLocation.getLatitude(), myLocation.getLongitude());
            }
        }, smsSendingPeriod, smsSendingPeriod);
    }

    @Override
    public void onDestroy() {
        mIsStarted = false;
        mSmsSendingTimer.cancel();
        mAlertReceiver.release();
        mLocationReceiver.release();

        String recordFilePath = mRecorder.stopRecording();

        Info info = new Info(mPreferenceManager.getReceiver(),
                mPreferenceManager.getCaller(),
                mLocationReceiver.getAppendedLocations(),
                mPreferenceManager.getCarNumber(),
                System.currentTimeMillis());

        String infoJsonString = new Gson().toJson(info);

        startService(new Intent(getApplicationContext(), UploadService.class)
                .setAction(UploadService.ACTION_UPLOAD)
                .putExtra("info", infoJsonString)
                .putExtra("recordFilePath", recordFilePath));

        EventBus.getDefault().post(new UploadStartEvent());

        super.onDestroy();
    }
}
