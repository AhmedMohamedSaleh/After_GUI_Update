package com.example.android.demo.remote;


import com.example.android.demo.Get_Connection_Status;

import java.util.concurrent.TimeUnit;


import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Establish_API_Connection {



    private static Retrofit retrofit = null;

    static OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(40, TimeUnit.SECONDS)
            .writeTimeout(40, TimeUnit.SECONDS)
            .readTimeout(40,TimeUnit.SECONDS).build();

    public static  Retrofit getClient(String url){
        if (retrofit == null){
            retrofit = new Retrofit.Builder().baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }


    public static Upload_Image_Request_Config getFileService(){
        return Establish_API_Connection.getClient(Get_Connection_Status.getUrl_api()).create(Upload_Image_Request_Config.class);
    }
    public static User_Images_Request_Config getUser_infos(){
        return Establish_API_Connection.getClient(Get_Connection_Status.getUrl_api()).create(User_Images_Request_Config.class);
    }
}
