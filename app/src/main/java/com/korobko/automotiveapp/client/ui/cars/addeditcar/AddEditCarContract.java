/*
 * Copyright (c) 2017.  | Volodymyr Korobko | volodymyr.korobko@gmail.com
 */

package com.korobko.automotiveapp.client.ui.cars.addeditcar;

import com.korobko.automotiveapp.BasePresenter;
import com.korobko.automotiveapp.BaseView;
import com.korobko.automotiveapp.models.Car;
import com.korobko.automotiveapp.models.RegistrationCard;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface AddEditCarContract {

    interface View extends BaseView<Presenter> {

        void showErrorEmptyField();

        void showCarsList();

        void showErrorLoadCar();

        void setCar(Car car);

        boolean isActive();

        void showErrorCarExist();
    }

    interface Presenter extends BasePresenter {

        void saveCar(String vIN, String make, String type, String color);

        void populateCar();

        boolean isDataMissing();
    }
}
