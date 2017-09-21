package com.helpapplication.http;

import com.helpapplication.http.model.Result;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 *  API °´Ã¼ »ý¼º
 */

public interface Api {

    @POST("info")
    @Multipart
    Call<Result> uploadData(@Part() MultipartBody.Part recordFile,
                            @Part() MultipartBody.Part infoJsonString);

    @POST("info/data_only")
    @FormUrlEncoded
    Call<Result> uploadDataWithoutRecordFile(@Field("info") String infoJsonString);
}
