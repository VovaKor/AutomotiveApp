/*
 * Copyright (c) 2017.  | Volodymyr Korobko | volodymyr.korobko@gmail.com
 */

package com.korobko.automotiveapp.client.repository;

import android.support.annotation.NonNull;

import com.korobko.automotiveapp.server.Driver;

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

    void getDrivers(@NonNull LoadDriversCallback callback);

    void saveDriver(@NonNull Driver driver);

    void getDriver(@NonNull String driverId, @NonNull GetDriverCallback callback);

    void deleteDriver(@NonNull String driverId);
}
