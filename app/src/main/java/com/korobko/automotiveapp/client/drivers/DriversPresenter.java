/*
 * Copyright (c) 2017.  | Volodymyr Korobko | volodymyr.korobko@gmail.com
 */

package com.korobko.automotiveapp.client.drivers;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.korobko.automotiveapp.client.repository.DriversDataSource;
import com.korobko.automotiveapp.client.repository.DriversRepository;
import com.korobko.automotiveapp.restapi.Driver;
import com.korobko.automotiveapp.utils.Constants;

import java.util.List;

/**
 * Created by vova on 10.08.17.
 * Listens to user actions from the UI ({@link DriversFragment}), retrieves the data and updates the
 * UI as required.
 */
public class DriversPresenter implements DriversContract.Presenter {

    private final DriversRepository mDriversRepository;

    private final DriversContract.View mDriversView;

    public DriversPresenter(@NonNull DriversRepository driversRepository, @NonNull DriversContract.View driversView) {
        mDriversRepository = driversRepository;
        mDriversView = driversView;

        mDriversView.setPresenter(this);
    }

    @Override
    public void start() {
        loadDrivers();
    }

    @Override
    public void result(int requestCode, int resultCode) {
        // If a driver was successfully added, show snackbar
        if (Constants.REQUEST_CODE_ADD_DRIVER == requestCode && Activity.RESULT_OK == resultCode) {
            mDriversView.showSuccessfullySavedMessage();
        }
    }

    @Override
    public void loadDrivers() {
        mDriversView.setLoadingIndicator(true);
        mDriversRepository.getDrivers(new DriversDataSource.LoadDriversCallback() {
            @Override
            public void onDriversLoaded(List<Driver> drivers) {

                // The view may not be able to handle UI updates anymore
                if (!mDriversView.isActive()) {
                    return;
                }
                mDriversView.setLoadingIndicator(false);
                mDriversView.showDrivers(drivers);
            }

            @Override
            public void onDataNotAvailable() {
                // The view may not be able to handle UI updates anymore
                if (!mDriversView.isActive()) {
                    return;
                }
                mDriversView.setLoadingIndicator(false);
                mDriversView.showLoadingDriversError();

            }
        });

    }

    @Override
    public void openDriverDetails(@NonNull Driver requestedDriver) {
        mDriversView.showDriverDetailsUi(requestedDriver.getId());
    }

    /**
     * Called by Data Binding library.
     */
    @Override
    public void addNewDriver() {
        mDriversView.showAddDriver();
    }
}

