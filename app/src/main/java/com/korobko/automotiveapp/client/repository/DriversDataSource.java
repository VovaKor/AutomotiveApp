/*
 * Copyright (c) 2017.  | Volodymyr Korobko | volodymyr.korobko@gmail.com
 */

package com.korobko.automotiveapp.client.repository;

import android.support.annotation.NonNull;

import com.korobko.automotiveapp.restapi.Driver;

import java.util.List;

/**
 * Main entry point for accessing drivers data.
 */
public interface DriversDataSource {

    interface LoadDriversCallback {

        void onDriversLoaded(List<Driver> drivers);

        void onDataNotAvailable();
    }

    interface GetDriverCallback {

        void onDriverLoaded(Driver driver);

        void onDataNotAvailable();
    }

    interface DeleteDriverCallback {

        void onSuccess();

        void onError();
    }
    interface SaveDriverCallback {

        void onSuccess();

        void onError();
    }

    void getDrivers(@NonNull LoadDriversCallback callback);

    void saveDriver(@NonNull Driver driver, @NonNull SaveDriverCallback callback);

    void createDriver(@NonNull Driver driver, @NonNull SaveDriverCallback callback);

    void getDriver(@NonNull String driverId, @NonNull GetDriverCallback callback);

    void deleteDriver(@NonNull String driverId, @NonNull DeleteDriverCallback callback);
}
