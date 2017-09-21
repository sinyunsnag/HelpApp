package com.helpapplication.config;

/**
 * Created by massivcode on 2017-02-25.
 */

public class AppConfig {
    // API 서버 ROOT URL
    public static final String API_BASE_URL = "http://wcwsbs.iptime.org:10869/api/";
    // AlertReceiver 에서 흔들림 카운팅을 위한 최소 임계치
    public static final int SHAKE_THRESHOLD = 800;
    // AlertReceiver 에서 위기상황 문자 발송을 위한 최소 카운트 수치
    public static final int COUNT_THRESHOLD = 10;
}
