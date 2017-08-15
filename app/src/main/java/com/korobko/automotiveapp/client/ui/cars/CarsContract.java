/*
 * Copyright (c) 2017.  | Volodymyr Korobko | volodymyr.korobko@gmail.com
 */

package com.korobko.automotiveapp.client.ui.cars;

import android.support.annotation.NonNull;

import com.korobko.automotiveapp.BasePresenter;
import com.korobko.automotiveapp.BaseView;
import com.korobko.automotiveapp.models.Car;
import com.korobko.automotiveapp.models.RegistrationCard;

import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface CarsContract {

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showAddCar();

        void showCarDetailsUi(String taskId);

        void showLoadingCarsError();

        void showCars(List<Car> cars);

        void showSuccessfullySavedMessage();

        boolean isActive();
    }

    interface Presenter extends BasePresenter {

        void result(int requestCode, int resultCode);

        void openCarDetails(@NonNull Car car);

        void addNewCar();

        void loadCars();

    }
}
