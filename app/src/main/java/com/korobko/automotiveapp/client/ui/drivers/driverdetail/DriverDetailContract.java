/*
 * Copyright (c) 2017.  | Volodymyr Korobko | volodymyr.korobko@gmail.com
 */

package com.korobko.automotiveapp.client.ui.drivers.driverdetail;


import com.korobko.automotiveapp.BasePresenter;
import com.korobko.automotiveapp.BaseView;
import com.korobko.automotiveapp.models.Driver;
import com.korobko.automotiveapp.models.RegistrationCard;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface DriverDetailContract {

    interface View extends BaseView<Presenter> {

        void showDriver(RegistrationCard card);

        void showErrorLoadDriver();

        void showDriverDeleted();

        boolean isActive();

        void showErrorDeleteDriver();

        void showCarsUI(String mCardId);
    }

    interface Presenter extends BasePresenter {

        void getDriver();

        void loadCars();

        void deleteDriver(String driverId);

    }
}
