/*
 * Copyright (c) 2017.  | Volodymyr Korobko | volodymyr.korobko@gmail.com
 */

package com.korobko.automotiveapp.client.repository;

import android.support.annotation.NonNull;

import com.korobko.automotiveapp.server.Driver;

import java.util.List;

/**
 * Concrete implementation to load drivers from the data source.
 */
public class DriversRepository implements DriversDataSource {

    private static DriversRepository INSTANCE = null;

    private final DriversDataSource mDriversRemoteDataSource;


    // Prevent direct instantiation.
    private DriversRepository(@NonNull DriversDataSource driversRemoteDataSource) {
        mDriversRemoteDataSource = driversRemoteDataSource;

    }

    /**
     * Returns the single instance of this class, creating it if necessary.
     * @param driversRemoteDataSource the backend data source
     * @return the {@link DriversRepository} instance
     */
    public static DriversRepository getInstance(DriversDataSource driversRemoteDataSource
                                                ) {
        if (INSTANCE == null) {
            INSTANCE = new DriversRepository(driversRemoteDataSource);
        }
        return INSTANCE;
    }


    /**
     * Gets all drivers from remote data source.
     */
    @Override
    public void getDrivers(@NonNull final LoadDriversCallback callback) {
        mDriversRemoteDataSource.getDrivers(new LoadDriversCallback() {
            @Override
            public void onDriversLoaded(List<Driver> drivers) {

                callback.onDriversLoaded(drivers);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void saveDriver(@NonNull Driver driver) {

        mDriversRemoteDataSource.saveDriver(driver);

    }

    /**
     * Gets driver with {@param driverId} from remote data source.
     */
    @Override
    public void getDriver(@NonNull final String driverId, @NonNull final GetDriverCallback callback) {
        mDriversRemoteDataSource.getDriver(driverId, new GetDriverCallback() {
            @Override
            public void onDriverLoaded(Driver driver) {

                callback.onDriverLoaded(driver);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void deleteDriver(@NonNull String driverId) {
        mDriversRemoteDataSource.deleteDriver(driverId);

    }

}
