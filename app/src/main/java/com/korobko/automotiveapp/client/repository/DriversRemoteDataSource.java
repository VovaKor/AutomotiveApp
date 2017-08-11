/*
 * Copyright (c) 2017.  | Volodymyr Korobko | volodymyr.korobko@gmail.com
 */

package com.korobko.automotiveapp.client.repository;

import android.support.annotation.NonNull;

import com.korobko.automotiveapp.server.Driver;

/**
 * Implementation of the remote data source.
 */
public class DriversRemoteDataSource implements DriversDataSource {

    private static DriversRemoteDataSource INSTANCE;

    public static DriversRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DriversRemoteDataSource();
        }
        return INSTANCE;
    }

    // Prevent direct instantiation.
    private DriversRemoteDataSource() {}


    @Override
    public void getDrivers(final @NonNull LoadDriversCallback callback) {

    }


    @Override
    public void getDriver(@NonNull String driverId, final @NonNull GetDriverCallback callback) {

    }

    @Override
    public void saveDriver(@NonNull Driver driver) {

    }

    @Override
    public void deleteDriver(@NonNull String driverId) {

    }
}
