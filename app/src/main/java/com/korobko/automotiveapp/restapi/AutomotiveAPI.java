/*
 * Copyright (c) 2017.  | Volodymyr Korobko | volodymyr.korobko@gmail.com
 */

package com.korobko.automotiveapp.restapi;

import com.korobko.automotiveapp.utils.Constants;

import java.util.List;

import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by vova on 11.08.17.
 */

public interface AutomotiveAPI {

    @GET(Constants.URL_GET_ALL_REGISTRATION_CARDS +Constants.SLASH+"{id}")
    Single<Driver> getDriverById(@Path("id") String driverId);

    @GET(Constants.URL_DELETE_REGISTRATION_CARD +Constants.SLASH+"{id}")
    Single<Driver> deleteDriverById(@Path("id") String driverId);

    @GET(Constants.URL_GET_ALL_REGISTRATION_CARDS)
    Single<List<Driver>> getDrivers();

    @POST(Constants.URL_ADD_REGISTRATION_CARD)
    Single<ResponseBody> createDriver(@Body Driver driver);

    @POST(Constants.URL_UPDATE_REGISTRATION_CARD)
    Single<ResponseBody> updateDriver(@Body Driver driver);

}
