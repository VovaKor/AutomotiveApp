/*
 * Copyright (c) 2017.  | Volodymyr Korobko | volodymyr.korobko@gmail.com
 */

package com.korobko.automotiveapp.client.ui.cars.cardetail;

import android.support.annotation.NonNull;

import com.korobko.automotiveapp.AutomotiveApp;
import com.korobko.automotiveapp.DataSource;
import com.korobko.automotiveapp.client.repository.Repository;
import com.korobko.automotiveapp.client.repository.shared.JsonPreference;
import com.korobko.automotiveapp.models.Car;
import com.korobko.automotiveapp.models.RegistrationCard;

import java.util.stream.IntStream;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * Listens to user actions from the UI ({@link CarDetailFragment}), retrieves the data and updates
 * the UI as required.
 */
public class CarDetailPresenter implements CarDetailContract.Presenter {

    private final CarDetailContract.View mCarDetailView;

    private JsonPreference mJsonPreference;
    private Disposable mDisposable;
    private RegistrationCard mCard;
    private DataSource mRepository;
    @NonNull
    private String mCarId;

    public CarDetailPresenter(@NonNull String carId, JsonPreference jsonPreference, Disposable disposable,
                              DataSource repository, @NonNull CarDetailContract.View view) {
        mJsonPreference = jsonPreference;
        mCarDetailView = view;
        mCarId = carId;
        mDisposable = disposable;
        mRepository = repository;
        mCarDetailView.setPresenter(this);
    }


    @Override
    public void start() {
        getCar();
    }

    @Override
    public void getCar() {
        Observable<RegistrationCard> cardObservable = Observable.just(
                loadRegistrationCardFromPref()
        );

        mDisposable = cardObservable.subscribe(card -> {
            Car car = getCarFromRegistrationCard(card, mCarId);

            // The view may not be able to handle UI updates anymore
                if (!mCarDetailView.isActive()) {
                    return;
                }
                if (car != null) {
                    mCarDetailView.showCar(car);
                } else {
                    mCarDetailView.showErrorLoadCar();
                }
             }
        );

    }

    private RegistrationCard loadRegistrationCardFromPref() {
        if(mJsonPreference.isSet()){
            String json = mJsonPreference.get();
            mCard = AutomotiveApp.getGson().fromJson(json, RegistrationCard.class);
            return mCard;
        }else {
            // The view may not be able to handle UI updates anymore
            if (!mCarDetailView.isActive()) {
                return null;
            }
            mCarDetailView.showErrorLoadCar();

            return null;
        }
    }

    @Override
    public void deleteCar(String carId) {
        Car car = getCarFromRegistrationCard(mCard, carId);
        mCard.getCars().remove(car);
        mRepository.saveRegistrationCard(mCard, new DataSource.GetCardCallback() {
            @Override
            public void onCardLoaded(RegistrationCard card) {
                mCarDetailView.showCarDeleted();
            }

            @Override
            public void onDataNotAvailable() {
                mCarDetailView.showErrorDeleteCar();
            }
        });

    }

    private Car getCarFromRegistrationCard(RegistrationCard card, String carId){
        return card.getCars().stream()
                .filter(c -> c.getVehicleIN().matches(carId))
                .findFirst()
                .get();
    }

}
