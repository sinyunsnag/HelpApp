package com.helpapplication.utils;

import android.content.Context;
import android.telephony.SmsManager;

import java.text.MessageFormat;

/**
 * Created by massivcode@gmail.com on 2017. 2. 25. 11:15
 */

public class SmsSender {
    private static final String SMS_FORMAT_ALERT = "{0}님께서 [{1}] 의 택시로 이동중 위험 신호를 보냈습니다!";
    private static final String SMS_FORMAT_LOCATION = "{0} 님의 현재 위치 : http://maps.google.com/maps?q={1},{2}";

    private static SmsSender sInstance = null;
    private SmsManager mSmsManager;
    private PreferenceManager mPreferenceManager;

    private SmsSender(Context context) {
        mSmsManager = SmsManager.getDefault();
        mPreferenceManager = PreferenceManager.getInstance(context);
    }

    public static synchronized SmsSender getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new SmsSender(context);
        }

        return sInstance;
    }

    public void sendAlertSmsMessage() {
        mSmsManager.sendTextMessage(mPreferenceManager.getReceiver(),
                null,
                MessageFormat.format(SMS_FORMAT_ALERT, mPreferenceManager.getCaller(), mPreferenceManager.getCarNumber()),
                null,
                null);
    }

    public void sendLocationSmsMessage(double latitude, double longitude) {
        mSmsManager.sendTextMessage(mPreferenceManager.getReceiver(),
                null,
                MessageFormat.format(SMS_FORMAT_LOCATION, mPreferenceManager.getCaller(), latitude, longitude),
                null,
                null);
    }


}
