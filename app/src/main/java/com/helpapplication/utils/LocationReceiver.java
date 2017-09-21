package com.helpapplication.utils;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by massivcode@gmail.com on 2017. 2. 25. 10:37
 */

public class LocationReceiver {
    public interface LocationCallback {
        void onLocationReceived(Location location);

        void onLocationAppended(List<LatLng> locationForGoogleMap);
    }

    private static LocationReceiver sInstance = null;

    private LocationManager mLocationManager;
    private LocationCallback mCallbackListener;

    private Location mCurrentLocation;

    private List<LatLng> mLocationsForGoogleMap = new ArrayList<>();


    private LocationReceiver(Context context, LocationCallback locationCallback) {
        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        mCallbackListener = locationCallback;
    }


    public static synchronized LocationReceiver getInstance(Context context, LocationCallback locationCallback) {
        if (sInstance == null) {
            sInstance = new LocationReceiver(context, locationCallback);
        }

        return sInstance;
    }

    public void requestLocation() {
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0.5f, mLocationListener);
    }

    public Location getCurrentLocation() {
        return mCurrentLocation;
    }

    public List<LatLng> getAppendedLocations() {
        return mLocationsForGoogleMap;
    }

    public void release() {
        mLocationManager.removeUpdates(mLocationListener);
        mLocationManager = null;
        mCurrentLocation = null;
        mCallbackListener = null;
        sInstance = null;
    }

    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location == null) {
                return;
            }

            mLocationsForGoogleMap.add(new LatLng(location.getLatitude(), location.getLongitude()));
            mCurrentLocation = location;

            if (mCallbackListener == null) {
                return;
            }

            mCallbackListener.onLocationReceived(mCurrentLocation);
            mCallbackListener.onLocationAppended(mLocationsForGoogleMap);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };
}
