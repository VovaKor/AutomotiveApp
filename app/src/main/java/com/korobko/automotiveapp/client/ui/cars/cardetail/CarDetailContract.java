/*
 * Copyright (c) 2017.  | Volodymyr Korobko | volodymyr.korobko@gmail.com
 */

package com.korobko.automotiveapp.client.ui.cars.cardetail;


import com.korobko.automotiveapp.BasePresenter;
import com.korobko.automotiveapp.BaseView;
import com.korobko.automotiveapp.models.Car;
import com.korobko.automotiveapp.models.Driver;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface CarDetailContract {

    interface View extends BaseView<Presenter> {

        void showCar(Car car);

        void showErrorLoadCar();

        void showCarDeleted();

        boolean isActive();

        void showErrorDeleteCar();

    }

    interface Presenter extends BasePresenter {

        void getCar();

        void deleteCar(String carId);

    }
}
