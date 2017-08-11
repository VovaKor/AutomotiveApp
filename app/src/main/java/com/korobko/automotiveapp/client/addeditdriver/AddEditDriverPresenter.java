/*
 * Copyright (c) 2017.  | Volodymyr Korobko | volodymyr.korobko@gmail.com
 */

package com.korobko.automotiveapp.client.addeditdriver;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.korobko.automotiveapp.client.repository.DriversDataSource;
import com.korobko.automotiveapp.server.Driver;

import static com.korobko.automotiveapp.utils.Constants.EMAIL_REGEXP;

/**
 * Listens to user actions from the UI ({@link AddEditDriverFragment}), retrieves the data and updates
 * the UI as required.
 */
public class AddEditDriverPresenter implements AddEditDriverContract.Presenter,
        DriversDataSource.GetDriverCallback {

    @NonNull
    private final DriversDataSource mDriversRepository;

    @NonNull
    private final AddEditDriverContract.View mAddEditDriverView;

    @Nullable
    private String mDriverId;

    private boolean mIsDataMissing;

    /**
     * Creates a presenter for the add/edit view.
     *
     * @param driverId ID of the driver to edit or null for a new task
     * @param driversRepository a repository of data for drivers
     * @param addEditDriverView the add/edit view
     * @param shouldLoadDataFromRepo whether data needs to be loaded or not (for config changes)
     */
    public AddEditDriverPresenter(@Nullable String driverId, @NonNull DriversDataSource driversRepository,
                                  @NonNull AddEditDriverContract.View addEditDriverView, boolean shouldLoadDataFromRepo) {
        mDriverId = driverId;
        mDriversRepository = driversRepository;
        mAddEditDriverView = addEditDriverView;
        mIsDataMissing = shouldLoadDataFromRepo;
    }

    @Override
    public void start() {
        if (!isNewDriver() && mIsDataMissing) {
            populateDriver();
        }
    }

    @Override
    public void saveDriver(String id, String firstName, String lastName, String licence, String phone) {

        if(TextUtils.isEmpty(id)
                ||TextUtils.isEmpty(firstName)
                ||TextUtils.isEmpty(lastName)
                ||TextUtils.isEmpty(licence)
                ||TextUtils.isEmpty(phone)){
            mAddEditDriverView.showErrorEmptyField();
        }else if (!id.matches(EMAIL_REGEXP)){
            mAddEditDriverView.showInvalidEmailError();
        }else {
            Driver driver = new Driver(id,firstName,lastName,phone,licence);
            if (isNewDriver()){
                mDriversRepository.getDriver(id, new DriversDataSource.GetDriverCallback() {
                    @Override
                    public void onDriverLoaded(Driver driver) {
                        mAddEditDriverView.showErrorEmailExist();
                    }

                    @Override
                    public void onDataNotAvailable() {

                        saveDriver(driver);
                    }
                });
            }else {
                saveDriver(driver);
            }


        }

    }

    @Override
    public void populateDriver() {
        if (isNewDriver()) {
            throw new RuntimeException("populateDriver() was called but driver is new.");
        }
        mDriversRepository.getDriver(mDriverId, this);
    }

    @Override
    public void onDriverLoaded(Driver driver) {
        // The view may not be able to handle UI updates anymore
        if (mAddEditDriverView.isActive()) {
            mAddEditDriverView.setDriver(driver);
        }
        mIsDataMissing = false;
    }

    @Override
    public void onDataNotAvailable() {
        // The view may not be able to handle UI updates anymore
        if (mAddEditDriverView.isActive()) {
            mAddEditDriverView.showErrorEmptyField();
        }
    }

    @Override
    public boolean isDataMissing() {
        return mIsDataMissing;
    }

    private boolean isNewDriver() {
        return mDriverId == null;
    }


    private void saveDriver(Driver driver) {

        mDriversRepository.saveDriver(driver);
        mAddEditDriverView.showDriversList(); // After an edit, go back to the list.
    }
}
