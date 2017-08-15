/*
 * Copyright (c) 2017.  | Volodymyr Korobko | volodymyr.korobko@gmail.com
 */

package com.korobko.automotiveapp.client.ui.cars.addeditcar;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.korobko.automotiveapp.AutomotiveApp;
import com.korobko.automotiveapp.DataSource;
import com.korobko.automotiveapp.client.repository.shared.JsonPreference;
import com.korobko.automotiveapp.models.Car;
import com.korobko.automotiveapp.models.Driver;
import com.korobko.automotiveapp.models.RegistrationCard;

import java.util.UUID;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * Listens to user actions from the UI ({@link AddEditCarFragment}), retrieves the data and updates
 * the UI as required.
 */
public class AddEditCarPresenter implements AddEditCarContract.Presenter{

    @NonNull
    private final DataSource mRepository;

    @NonNull
    private final AddEditCarContract.View mAddEditCarView;

    @Nullable
    private String mCarId;
    private RegistrationCard mRegistrationCard;
    private JsonPreference mJsonPreference;
    private boolean mIsDataMissing;
    private Disposable mDisposable;

    /**
     * Creates a presenter for the add/edit view.
     * @param carId ID of the car to edit or null for a new car
     * @param repository a repository of data for drivers
     * @param addEditCarView the add/edit view
     * @param disposable needs to be disposed on onStop() to prevent memory leaks
     * @param jsonPreference a shared preferences where a RegistrationCard is saved
     * @param shouldLoadDataFromRepo whether data needs to be loaded or not (for config changes)
     */
    public AddEditCarPresenter(@Nullable String carId, @NonNull DataSource repository,
                               @NonNull AddEditCarContract.View addEditCarView, Disposable disposable, JsonPreference jsonPreference, boolean shouldLoadDataFromRepo) {
        mCarId = carId;
        mRepository = repository;
        mAddEditCarView = addEditCarView;
        mJsonPreference = jsonPreference;
        mDisposable = disposable;
        mIsDataMissing = shouldLoadDataFromRepo;
    }

    @Override
    public void start() {
        if (!isNewCar() && mIsDataMissing) {
            populateCar();
        }
    }

    @Override
    public void saveCar(String vIN, String make, String type, String color) {

        if(TextUtils.isEmpty(vIN)
                ||TextUtils.isEmpty(make)
                ||TextUtils.isEmpty(type)
                ||TextUtils.isEmpty(color)){
            mAddEditCarView.showErrorEmptyField();
        }else {
            if (isNewCar()){
                mDisposable = getCardObservable().subscribe(card -> {
                            Car car = getCarFromRegistrationCard(card, vIN);

                            // The view may not be able to handle UI updates anymore
                            if (!mAddEditCarView.isActive()) {
                                return;
                            }
                            if (car != null) {
                                mAddEditCarView.showErrorCarExist();
                            } else {
                                Car carToSave = new Car(vIN, make, type, color, card.getRegistrationNumber());
                                card.getCars().add(carToSave);
                                saveCard(card);

                            }
                        }
                );

            }else {
                if (mRegistrationCard!=null) {
                    Car car = getCarFromRegistrationCard(mRegistrationCard, vIN);
                    car.setMake(make);
                    car.setType(type);
                    car.setColor(color);
                    saveCard(mRegistrationCard);
                }
            }


        }

    }

    @Override
    public void populateCar() {
        if (isNewCar()) {
            throw new RuntimeException("populateCar() was called but car is new.");
        }

        mDisposable = getCardObservable().subscribe(card -> {
                    Car car = getCarFromRegistrationCard(card, mCarId);

                    // The view may not be able to handle UI updates anymore
                    if (!mAddEditCarView.isActive()) {
                        return;
                    }
                    if (car != null) {
                        mAddEditCarView.setCar(car);
                    } else {
                        mAddEditCarView.showErrorLoadCar();
                    }
                }
        );
    }

    @Override
    public boolean isDataMissing() {
        return mIsDataMissing;
    }

    private boolean isNewCar() {
        return mCarId == null;
    }


    private void saveCard(RegistrationCard card) {

        mRepository.saveRegistrationCard(card, new DataSource.GetCardCallback() {
            @Override
            public void onCardLoaded(RegistrationCard registrationCard) {
                // After an edit, go back to the list.
                mAddEditCarView.showCarsList();
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

                mAddEditCarView.showCarsList();
            }

            @Override
            public void onError() {

            }
        });

    }
    private Observable<RegistrationCard> getCardObservable(){
        return Observable.just(
                loadRegistrationCardFromPref()
        );
    }

    private RegistrationCard loadRegistrationCardFromPref() {
        if(mJsonPreference.isSet()){
            String json = mJsonPreference.get();
            mRegistrationCard = AutomotiveApp.getGson().fromJson(json, RegistrationCard.class);
            return mRegistrationCard;
        }else {
            // The view may not be able to handle UI updates anymore
            if (!mAddEditCarView.isActive()) {
                return null;
            }
            mAddEditCarView.showErrorLoadCar();

            return null;
        }
    }
    private Car getCarFromRegistrationCard(RegistrationCard card, String carId){
        return card.getCars().stream()
                .filter(c -> c.getVehicleIN().matches(carId))
                .findFirst()
                .get();
    }
}
