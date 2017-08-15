/*
 * Copyright (c) 2017.  | Volodymyr Korobko | volodymyr.korobko@gmail.com
 */

package com.korobko.automotiveapp.client.ui.drivers.driverdetail;

import android.support.annotation.NonNull;


import com.korobko.automotiveapp.DataSource;
import com.korobko.automotiveapp.client.repository.Repository;
import com.korobko.automotiveapp.models.Driver;
import com.korobko.automotiveapp.models.RegistrationCard;

/**
 * Listens to user actions from the UI ({@link DriverDetailFragment}), retrieves the data and updates
 * the UI as required.
 */
public class DriverDetailPresenter implements DriverDetailContract.Presenter {

    private final DriverDetailContract.View mDriverDetailView;

    private Repository mRepository;

    @NonNull
    private String mCardId;

    public DriverDetailPresenter(@NonNull String cardId, Repository repository,
                                 @NonNull DriverDetailContract.View view) {
        mRepository = repository;
        mDriverDetailView = view;
        mCardId = cardId;

        mDriverDetailView.setPresenter(this);
    }


    @Override
    public void start() {
        getDriver();
    }

    @Override
    public void getDriver() {
        mRepository.getRegistrationCard(mCardId, new DataSource.GetCardCallback() {
            @Override
            public void onCardLoaded(RegistrationCard card) {
                // The view may not be able to handle UI updates anymore
                if (!mDriverDetailView.isActive()) {
                    return;
                }
                if (card != null) {
                    mDriverDetailView.showDriver(card.getDriver());
                } else {
                    mDriverDetailView.showErrorLoadDriver();
                }
            }

            @Override
            public void onDataNotAvailable() {
                // The view may not be able to handle UI updates anymore
                if (!mDriverDetailView.isActive()) {
                    return;
                }
                mDriverDetailView.showErrorLoadDriver();
            }
        });
    }

    @Override
    public void loadCars(){
        mDriverDetailView.showCarsUI(mCardId);
    }

    @Override
    public void deleteDriver(String cardId) {
        mRepository.deleteRegistrationCard(cardId, new DataSource.DeleteCardCallback() {
            @Override
            public void onSuccess() {
                mDriverDetailView.showDriverDeleted();
            }

            @Override
            public void onError() {
                mDriverDetailView.showErrorDeleteDriver();
            }
        });

    }

}
