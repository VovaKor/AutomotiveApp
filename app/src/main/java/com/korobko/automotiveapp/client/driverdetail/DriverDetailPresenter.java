/*
 * Copyright (c) 2017.  | Volodymyr Korobko | volodymyr.korobko@gmail.com
 */

package com.korobko.automotiveapp.client.driverdetail;

import android.support.annotation.NonNull;


import com.korobko.automotiveapp.client.repository.DriversDataSource;
import com.korobko.automotiveapp.client.repository.DriversRepository;
import com.korobko.automotiveapp.server.Driver;

/**
 * Listens to user actions from the UI ({@link DriverDetailFragment}), retrieves the data and updates
 * the UI as required.
 */
public class DriverDetailPresenter implements DriverDetailContract.Presenter {

    private final DriverDetailContract.View mDriverDetailView;

    private DriversRepository mRepository;

    @NonNull
    private String mDriverId;

    public DriverDetailPresenter(@NonNull String driverId, DriversRepository repository,
                                 @NonNull DriverDetailContract.View view) {
        mRepository = repository;
        mDriverDetailView = view;
        mDriverId = driverId;

        mDriverDetailView.setPresenter(this);
    }


    @Override
    public void start() {
        getDriver();
    }

    @Override
    public void getDriver() {
        mRepository.getDriver(mDriverId, new DriversDataSource.GetDriverCallback() {
            @Override
            public void onDriverLoaded(Driver driver) {
                // The view may not be able to handle UI updates anymore
                if (!mDriverDetailView.isActive()) {
                    return;
                }
                if (driver != null) {
                    mDriverDetailView.showDriver(driver);
                } else {
                    mDriverDetailView.showError();
                }
            }

            @Override
            public void onDataNotAvailable() {
                // The view may not be able to handle UI updates anymore
                if (!mDriverDetailView.isActive()) {
                    return;
                }
                mDriverDetailView.showError();
            }
        });
    }

    @Override
    public void deleteDriver(String driverId) {
        mRepository.deleteDriver(driverId);
        mDriverDetailView.showDriverDeleted();
    }

}
