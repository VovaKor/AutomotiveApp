/*
 * Copyright (c) 2017.  | Volodymyr Korobko | volodymyr.korobko@gmail.com
 */

package com.korobko.automotiveapp.client.ui.drivers.addeditdriver;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.korobko.automotiveapp.DataSource;
import com.korobko.automotiveapp.models.Driver;
import com.korobko.automotiveapp.models.RegistrationCard;

import java.util.UUID;

import static com.korobko.automotiveapp.utils.Constants.REGEXP_EMAIL;


/**
 * Listens to user actions from the UI ({@link AddEditDriverFragment}), retrieves the data and updates
 * the UI as required.
 */
public class AddEditDriverPresenter implements AddEditDriverContract.Presenter,
        DataSource.GetCardCallback {

    @NonNull
    private final DataSource mRepository;

    @NonNull
    private final AddEditDriverContract.View mAddEditDriverView;

    @Nullable
    private String mCardId;
    private RegistrationCard mRegistrationCard;

    private boolean mIsDataMissing;

    /**
     * Creates a presenter for the add/edit view.
     *
     * @param cardId ID of the card to edit or null for a new card
     * @param repository a repository of data for drivers
     * @param addEditDriverView the add/edit view
     * @param shouldLoadDataFromRepo whether data needs to be loaded or not (for config changes)
     */
    public AddEditDriverPresenter(@Nullable String cardId, @NonNull DataSource repository,
                                  @NonNull AddEditDriverContract.View addEditDriverView, boolean shouldLoadDataFromRepo) {
        mCardId = cardId;
        mRepository = repository;
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
        }else if (!id.matches(REGEXP_EMAIL)){
            mAddEditDriverView.showInvalidEmailError();
        }else {

            if (isNewDriver()){
                mRepository.getRegistrationCard(id, new DataSource.GetCardCallback() {

                    @Override
                    public void onCardLoaded(RegistrationCard card) {
                            mRegistrationCard = card;
                            mAddEditDriverView.showErrorEmailExist();

                    }

                    @Override
                    public void onDataNotAvailable() {
                        Driver driverToSave = new Driver(UUID.randomUUID().toString(),firstName,lastName,phone,licence);
                        RegistrationCard card = new RegistrationCard();
                        card.setRegistrationNumber(id);
                        card.setDriver(driverToSave);
                        createCard(card);
                    }


                });
            }else {
                if (mRegistrationCard!=null) {
                    Driver driverToSave = mRegistrationCard.getDriver();
                    driverToSave.setFirstName(firstName);
                    driverToSave.setLastName(lastName);
                    driverToSave.setLicenceNumber(licence);
                    driverToSave.setPhone(phone);

                    saveCard(mRegistrationCard);
                }
            }


        }

    }

    @Override
    public void populateDriver() {
        if (isNewDriver()) {
            throw new RuntimeException("populateDriver() was called but driver is new.");
        }
        mRepository.getRegistrationCard(mCardId, this);
    }

    @Override
    public void onCardLoaded(RegistrationCard card) {
        // The view may not be able to handle UI updates anymore
        if (mAddEditDriverView.isActive()) {
            mAddEditDriverView.setCard(card);
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
        return mCardId == null;
    }


    private void saveCard(RegistrationCard card) {

        mRepository.saveRegistrationCard(card, new DataSource.GetCardCallback() {
            @Override
            public void onCardLoaded(RegistrationCard registrationCard) {
                // After an edit, go back to the list.
                mAddEditDriverView.showDriversList();
            }

            @Override
            public void onDataNotAvailable() {

            }
        });

    }

    private void createCard(RegistrationCard card) {
        mRepository.createRegistrationCard(card, new DataSource.SaveCardCallback() {
            @Override
            public void onSuccess() {

                mAddEditDriverView.showDriversList();
            }

            @Override
            public void onError() {

            }
        });

    }
}
