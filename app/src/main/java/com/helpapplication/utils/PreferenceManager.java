package com.helpapplication.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by massivcode@gmail.com on 2017. 2. 25. 09:58
 */

public class PreferenceManager {
    // 보호자 (문자 메세지 수신자) 전화번호
    private static final String ITEM_RECEIVER = "ITEM_RECEIVER";
    // 사용자 (문자 메세지 발신자) 전화번호
    private static final String ITEM_CALLER = "ITEM_CALLER";

    // 서비스 실행주기 1분, 3분, 5분
    private static final String ITEM_SERVICE_PERIOD_ONE_MINUTE = "ITEM_SERVICE_PERIOD_ONE_MINUTE";
    private static final String ITEM_SERVICE_PERIOD_THREE_MINUTE = "ITEM_SERVICE_PERIOD_THREE_MINUTE";
    private static final String ITEM_SERVICE_PERIOD_FIVE_MINUTE = "ITEM_SERVICE_PERIOD_FIVE_MINUTE";

    // 알람 서비스 사용여부
    private static final String ITEM_SERVICE_USES = "ITEM_SERVICE_USES";
    private static final String ITEM_SERVICE_UNUSES = "ITEM_SERVICE_UNUSES";

    private static final String ITEM_CAR_NUMBER = "ITEM_CAR_NUMBER";


    private static PreferenceManager sInstance = null;
    private SharedPreferences mPref;

    private PreferenceManager(Context context) {
        mPref = context.getSharedPreferences("help.pref", Context.MODE_PRIVATE);
    }

    public static synchronized PreferenceManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new PreferenceManager(context);
        }

        return sInstance;
    }

    public String getReceiver() {
        return mPref.getString(ITEM_RECEIVER, null);
    }

    public void setReceiver(String receiver) {
        mPref.edit().putString(ITEM_RECEIVER, receiver).apply();
    }

    public String getCaller() {
        return mPref.getString(ITEM_CALLER, "");
    }

    public void setCaller(String caller) {
        mPref.edit().putString(ITEM_CALLER, caller).apply();
    }

    public boolean isServicePeriodOneMinute() {
        return mPref.getBoolean(ITEM_SERVICE_PERIOD_ONE_MINUTE, false);
    }

    public void setIsServicePeriodOneMinute(boolean isServicePeriodOneMinute) {
        mPref.edit().putBoolean(ITEM_SERVICE_PERIOD_ONE_MINUTE, isServicePeriodOneMinute).apply();
    }

    public boolean isServicePeriodThreeMinute() {
        return mPref.getBoolean(ITEM_SERVICE_PERIOD_THREE_MINUTE, false);
    }

    public void setIsServicePeriodThreeMinute(boolean isServicePeriodThreeMinute) {
        mPref.edit().putBoolean(ITEM_SERVICE_PERIOD_THREE_MINUTE, isServicePeriodThreeMinute).apply();
    }

    public boolean isServicePeriodFiveMinute() {
        return mPref.getBoolean(ITEM_SERVICE_PERIOD_FIVE_MINUTE, false);
    }

    public void setIsServicePeriodFiveMinute(boolean isServicePeriodFiveMinute) {
        mPref.edit().putBoolean(ITEM_SERVICE_PERIOD_FIVE_MINUTE, isServicePeriodFiveMinute).apply();
    }

    public boolean isServiceUses() {
        return mPref.getBoolean(ITEM_SERVICE_USES, false);
    }

    public void setIsServiceUses(boolean isServiceUses) {
        mPref.edit().putBoolean(ITEM_SERVICE_USES, isServiceUses).apply();
    }

    public boolean isServiceUnUses() {
        return mPref.getBoolean(ITEM_SERVICE_UNUSES, false);
    }

    public void setIsServiceUnUses(boolean isServiceUnUses) {
        mPref.edit().putBoolean(ITEM_SERVICE_UNUSES, isServiceUnUses).apply();
    }

    public String getCarNumber() {
        return mPref.getString(ITEM_CAR_NUMBER, null);
    }

    public void setCarNumber(String carNumber) {
        mPref.edit().putString(ITEM_CAR_NUMBER, carNumber).apply();
    }


}
