/*
 * Copyright (c) 2017.  | Volodymyr Korobko | volodymyr.korobko@gmail.com
 */

package com.korobko.automotiveapp;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.korobko.automotiveapp.restapi.AutomotiveAPI;
import com.korobko.automotiveapp.utils.Constants;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by vova on 11.08.17.
 */

public class AutomotiveApp extends Application {
    private static AutomotiveAPI automotiveAPI;
    private Retrofit retrofit;
    private static Gson gson;

    @Override
    public void onCreate() {
        super.onCreate();
        gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.URL_BASE)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        automotiveAPI = retrofit.create(AutomotiveAPI.class);
    }

    public static AutomotiveAPI getAutomotiveAPI() {
        return automotiveAPI;
    }

    public static Gson getGson() {
        return gson;
    }
}
