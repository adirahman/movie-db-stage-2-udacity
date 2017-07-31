package com.example.user.moviedbstage2udacity.api;

import com.example.user.moviedbstage2udacity.Constants;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by user on 7/30/17.
 */

public class RetrofitClient {
    private static RetrofitClient apiInstance;
    private ApiService apiService;
    private Retrofit retrofit;

    public RetrofitClient(){

    }

    public void init(){
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client.addInterceptor(loggingInterceptor);

        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(client.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static RetrofitClient getApiInstance(){
        if(apiInstance == null){
            apiInstance = new RetrofitClient();
            apiInstance.init();
        }
        return apiInstance;
    }
    public ApiService getApiService(){
        if(apiService == null){
            apiService = retrofit.create(ApiService.class);
        }
        return apiService;
    }
}
