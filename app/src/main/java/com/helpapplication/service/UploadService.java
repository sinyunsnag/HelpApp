package com.helpapplication.service;

import android.app.IntentService;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import com.helpapplication.events.UploadCompletedEvent;
import com.helpapplication.http.HttpHelper;
import com.helpapplication.http.model.Result;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

/**
 * Created by massivcode on 2017-02-25.
 */

public class UploadService extends IntentService {
    private static final String TAG = UploadService.class.getSimpleName();
    public static final String ACTION_UPLOAD = "ACTION_UPLOAD";

    public UploadService() {
        super(UploadService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null && intent.getAction() != null) {
            switch (intent.getAction()) {
                case ACTION_UPLOAD:
                    String infoJsonString = intent.getStringExtra("info");

                    if (TextUtils.isEmpty(infoJsonString)) {
                        Log.e(TAG, "onHandleIntent: 업로드 관련 데이터가 존재하지 않습니다!");
                        return;
                    }


                    String recordFilePath = intent.getStringExtra("recordFilePath");

                    if (TextUtils.isEmpty(recordFilePath)) {
                        try {
                            Response<Result> response = HttpHelper.getApi().uploadDataWithoutRecordFile(infoJsonString).execute();
                            EventBus.getDefault().post(new UploadCompletedEvent(response.isSuccessful(), response.code(), response.body()));
                        } catch (IOException e) {
                            Log.e(TAG, "onHandleIntent: 서버와의 통신이 원활하지 않습니다!");
                            EventBus.getDefault().post(new UploadCompletedEvent(false, 500, null));
                        }
                    } else {
                        File recordFile = new File(recordFilePath);

                        RequestBody requestBody = RequestBody.create(MediaType.parse("audio/mp4"), recordFile);
                        MultipartBody.Part recordFileBody = MultipartBody.Part.createFormData("recordFile", recordFile.getName(), requestBody);
                        MultipartBody.Part info = MultipartBody.Part.createFormData("info", infoJsonString);
                        Log.d(TAG, "onHandleIntent: " + infoJsonString);


                        try {
                            Response<Result> response = HttpHelper.getApi().uploadData(recordFileBody, info).execute();
                            EventBus.getDefault().post(new UploadCompletedEvent(response.isSuccessful(), response.code(), response.body()));
                        } catch (IOException e) {
                            Log.e(TAG, "onHandleIntent: 서버와의 통신이 원활하지 않습니다!");
                            EventBus.getDefault().post(new UploadCompletedEvent(false, 500, null));
                        }
                    }


                    break;
            }
        }
    }
}
