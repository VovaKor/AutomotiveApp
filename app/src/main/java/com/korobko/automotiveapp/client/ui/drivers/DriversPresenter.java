/*
 * Copyright (c) 2017.  | Volodymyr Korobko | volodymyr.korobko@gmail.com
 */

package com.korobko.automotiveapp.client.ui.drivers;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.korobko.automotiveapp.DataSource;
import com.korobko.automotiveapp.client.repository.Repository;
import com.korobko.automotiveapp.models.Driver;
import com.korobko.automotiveapp.models.RegistrationCard;
import com.korobko.automotiveapp.utils.Constants;

import java.util.List;

/**
 * Created by vova on 10.08.17.
 * Listens to user actions from the UI ({@link DriversFragment}), retrieves the data and updates the
 * UI as required.
 */
public class DriversPresenter implements DriversContract.Presenter {

    private final Repository mRepository;

    private final DriversContract.View mDriversView;

    public DriversPresenter(@NonNull Repository repository, @NonNull DriversContract.View driversView) {
        mRepository = repository;
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
        mRepository.getRegistrationCards(new DataSource.LoadCardsCallback() {
            @Override
            public void onCardsLoaded(List<RegistrationCard> cards) {

                // The view may not be able to handle UI updates anymore
                if (!mDriversView.isActive()) {
                    return;
                }
                mDriversView.setLoadingIndicator(false);
                mDriversView.showDrivers(cards);
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
    public void openDriverDetails(@NonNull RegistrationCard card) {
        mDriversView.showDriverDetailsUi(card.getRegistrationNumber());
    }

    /**
     * Called by Data Binding library.
     */
    @Override
    public void addNewDriver() {
        mDriversView.showAddDriver();
    }
}

