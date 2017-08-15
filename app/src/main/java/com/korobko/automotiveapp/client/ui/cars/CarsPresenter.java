/*
 * Copyright (c) 2017.  | Volodymyr Korobko | volodymyr.korobko@gmail.com
 */

package com.korobko.automotiveapp.client.ui.cars;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.korobko.automotiveapp.AutomotiveApp;
import com.korobko.automotiveapp.DataSource;
import com.korobko.automotiveapp.client.repository.Repository;
import com.korobko.automotiveapp.client.repository.shared.JsonPreference;
import com.korobko.automotiveapp.models.Car;
import com.korobko.automotiveapp.models.RegistrationCard;
import com.korobko.automotiveapp.utils.Constants;

/**
 * Created by vova on 10.08.17.
 * Listens to user actions from the UI ({@link CarsFragment}), retrieves the data and updates the
 * UI as required.
 */
public class CarsPresenter implements CarsContract.Presenter {

    private final Repository mRepository;
    private final String mCardId;
    private final CarsContract.View mDriversView;
    private RegistrationCard mCard;
    private JsonPreference mJsonPreference;

    public CarsPresenter(@NonNull String cardId, @NonNull JsonPreference jsonPreference, @NonNull Repository repository, @NonNull CarsContract.View driversView) {
        mRepository = repository;
        mDriversView = driversView;
        mCardId = cardId;
        mJsonPreference = jsonPreference;
        mDriversView.setPresenter(this);
    }

    @Override
    public void start() {
        loadCars();
    }

    @Override
    public void result(int requestCode, int resultCode) {
        // If a car was successfully added, show snackbar
        if (Constants.REQUEST_CODE_ADD_CAR == requestCode && Activity.RESULT_OK == resultCode) {
            mDriversView.showSuccessfullySavedMessage();
        }
    }

    @Override
    public void loadCars() {
        mDriversView.setLoadingIndicator(true);
        mRepository.getRegistrationCard(mCardId, new DataSource.GetCardCallback() {
            @Override
            public void onCardLoaded(RegistrationCard card) {

                // The view may not be able to handle UI updates anymore
                if (!mDriversView.isActive()) {
                    return;
                }
                mDriversView.setLoadingIndicator(false);
                mCard = card;
                mDriversView.showCars(card.getCars());
            }

            @Override
            public void onDataNotAvailable() {
                // The view may not be able to handle UI updates anymore
                if (!mDriversView.isActive()) {
                    return;
                }
                mDriversView.setLoadingIndicator(false);
                mDriversView.showLoadingCarsError();

            }
        });

    }

    @Override
    public void openCarDetails(@NonNull Car car) {
        saveCardToPreference();
        mDriversView.showCarDetailsUi(car.getVehicleIN());
    }

    /**
     * Called by Data Binding library.
     */
    @Override
    public void addNewCar() {
        saveCardToPreference();
        mDriversView.showAddCar();
    }

    private void saveCardToPreference(){
        mJsonPreference.set(AutomotiveApp.getGson().toJson(mCard, RegistrationCard.class));
    }
}

