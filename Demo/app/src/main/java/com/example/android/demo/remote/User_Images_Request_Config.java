package com.example.android.demo.remote;

import org.json.JSONObject;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

import static android.R.id.message;

public interface User_Images_Request_Config {

    @Multipart
    @POST("getallimages")
    public abstract Call<ResponseBody> get_images(@Part MultipartBody.Part token);
}
