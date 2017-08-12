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
import retrofit2.http.Url;

/**
 * Created by vova on 11.08.17.
 */

public interface AutomotiveAPI {

    @GET(Constants.URL_DRIVERS +Constants.SLASH+"{id}")
    Single<Driver> getDriverById(@Path("id") String driverId);

    @GET(Constants.URL_DRIVERS_DELETE +Constants.SLASH+"{id}")
    Single<Driver> deleteDriverById(@Path("id") String driverId);

    @GET(Constants.URL_DRIVERS)
    Single<List<Driver>> getDrivers();

    @POST(Constants.URL_DRIVERS_ADD)
    Single<ResponseBody> createDriver(@Body Driver driver);

    @POST(Constants.URL_DRIVERS_UPDATE)
    Single<ResponseBody> updateDriver(@Body Driver driver);

}
