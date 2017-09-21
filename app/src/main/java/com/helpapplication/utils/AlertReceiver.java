package com.helpapplication.utils;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.Timer;
import java.util.TimerTask;

import com.helpapplication.config.AppConfig;

import static android.content.Context.SENSOR_SERVICE;

/**
 * Created by massivcode on 2017-02-25.
 */

public class AlertReceiver {

    public interface Callback {
        void onAlertReceived();
    }

    private static AlertReceiver sInstance = null;

    // Alert CallbackListener
    private Callback mAlertCallbackListener;

    // 가속도 센서 관련 변수
    private SensorManager mSensorManager;
    private Sensor mAccelerometerSensor;

    private long mLastCheckTime; // 마지막 체크 Time
    private float mSpeed; // 가속도 값
    private float mLastX; // 마지막 x 좌표 값
    private float mLastY; // 마지막 y 좌표 값
    private float mLastZ; // 마지막 z 좌표 값

    private float x, y, z; // 현재 x,y,z 좌표 값


    // 중력가속도 x,y,z 좌표값
    private static final int DATA_X = SensorManager.DATA_X;
    private static final int DATA_Y = SensorManager.DATA_Y;
    private static final int DATA_Z = SensorManager.DATA_Z;

    // 가속도 관련 횟수 Count 변수
    private int mCount;

    // 타이머
    private Timer mAlertGenerateTimer;


    private AlertReceiver(Context context, Callback callback) {
        mAlertCallbackListener = callback;
        mSensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    public static synchronized AlertReceiver getInstance(Context context, Callback callback) {
        if (sInstance == null) {
            sInstance = new AlertReceiver(context, callback);
        }

        return sInstance;
    }

    public void start() {
        if (mAccelerometerSensor != null) {
            mSensorManager.registerListener(mSensorEventListener, mAccelerometerSensor, SensorManager.SENSOR_DELAY_GAME);
            mAlertGenerateTimer = new Timer();

            mAlertGenerateTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (mCount >= AppConfig.COUNT_THRESHOLD) {
                        mAlertCallbackListener.onAlertReceived();
                        mCount = 0;
                    }
                }
            }, 5000, 5000);
        }
    }

    public void release() {
        if (mSensorManager != null) {
            mAlertGenerateTimer.cancel();
            mSensorManager.unregisterListener(mSensorEventListener);
        }
    }

    private SensorEventListener mSensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                long currentTime = System.currentTimeMillis(); // 현재 시간 체크
                long gabOfTime = (currentTime - mLastCheckTime); // 좌표를 체크할 사이시간 설정

                if (gabOfTime > 100) { // 100ms 이상의 시간이 지낫으면 x,y,z값 체크
                    mLastCheckTime = currentTime;

                    x = sensorEvent.values[SensorManager.DATA_X];
                    y = sensorEvent.values[SensorManager.DATA_Y];
                    z = sensorEvent.values[SensorManager.DATA_Z];

                    mSpeed = Math.abs(x + y + z - mLastX - mLastY - mLastZ) / gabOfTime * 10000; // 중력 가속도 계산

                    if (mSpeed > AppConfig.SHAKE_THRESHOLD) { // 일정 수준 이상의 속력이라면 카운트 증가
                        mCount++;
                    }

                    // x,y,z 좌표값 저장
                    mLastX = sensorEvent.values[DATA_X];
                    mLastY = sensorEvent.values[DATA_Y];
                    mLastZ = sensorEvent.values[DATA_Z];
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };


}
