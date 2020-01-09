package com.chooloo.www.callmanager.proof.aitech.network.retrofit;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {
    private static Retrofit retrofit = null;

    static Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .readTimeout(120, TimeUnit.SECONDS)
            .connectTimeout(180, TimeUnit.SECONDS)
            .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build();

    public static Retrofit getClient(Context context, String apiBaseUrl) throws IllegalArgumentException {

        Log.d("Apiclient getClient()", apiBaseUrl);
        if (apiBaseUrl != null) {
            if (retrofit == null) {
                retrofit = new Retrofit.Builder()
                        .baseUrl(apiBaseUrl)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .client(okHttpClient)
                        .build();
            }
        }
        return retrofit;

    }


    public static Retrofit getCallProofClient(String apiBaseUrl) throws IllegalArgumentException {

        Log.d("Apiclient CallProof", apiBaseUrl);
        if (apiBaseUrl != null) {
            if (retrofit == null) {
                retrofit = new Retrofit.Builder()
                        .baseUrl(apiBaseUrl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(okHttpClient)
                        .build();
            }
        }
        return retrofit;

    }
}
