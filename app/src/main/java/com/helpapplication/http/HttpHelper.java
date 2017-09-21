package com.helpapplication.http;

import com.helpapplication.config.AppConfig;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 *  레트로피트를 이용하여 웹과 연결
 */

public class HttpHelper {
    private static Retrofit createRetrofit() {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(AppConfig.API_BASE_URL)
                .client(getClient())
                .build();
    }

    private static OkHttpClient getClient() {
        return new OkHttpClient.Builder().build();
    }

    public static Api getApi() {
        Retrofit retrofit = createRetrofit();
        return retrofit.create(Api.class);
    }
}
