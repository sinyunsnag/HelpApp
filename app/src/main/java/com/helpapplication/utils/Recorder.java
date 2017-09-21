package com.helpapplication.utils;

import android.media.MediaRecorder;
import android.util.Log;

import java.io.IOException;

/**
 * Created by massivcode on 2017-02-25.
 */

public class Recorder {
    private static final String TAG = Recorder.class.getSimpleName();
    private static Recorder sInstance = null;

    private MediaRecorder mMediaRecorder;
    private String mRecordFilePath;
    private boolean mIsRecording = false;

    private Recorder() {
        mMediaRecorder = new MediaRecorder();
    }

    public static synchronized Recorder getInstance() {
        if (sInstance == null) {
            sInstance = new Recorder();
        }

        return sInstance;
    }

    public void startRecord() {
        mRecordFilePath = FileHelper.APP_FOLDER_PATH + System.currentTimeMillis() + "_record.mp4";

        if (mMediaRecorder == null) {
            mMediaRecorder = new MediaRecorder();
        }

        FileHelper.createDirectory();

        // 오디오 입력 소스를 마이크로 설정
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        // 녹음 파일의 출력 포맷을 설정
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        // 지정된 경로로 파일 생성
        mMediaRecorder.setOutputFile(mRecordFilePath);
        // 오디오 인코더 설정
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

        try {
            // 레코더 준비
            mMediaRecorder.prepare();
        } catch (IOException e) {
            Log.e(TAG, "MediaRecorder 초기화 실패!");
        }

        // 녹음 시작
        mMediaRecorder.start();
        mIsRecording = true;
    }

    public boolean isRecording() {
        return mIsRecording;
    }

    public String stopRecording() {
        mIsRecording = false;

        // 녹음 종료
        if (mMediaRecorder != null) {
            // prepare 되기 전에 stop 할 경우 예외 발생함
            try {
                mMediaRecorder.stop();
                // 레코더 리소스 반환
                mMediaRecorder.release();
                mMediaRecorder = null;

            } catch (Exception e) {
                return null;
            }

            return mRecordFilePath;
        }

        return null;
    }
}
