/*
 * Copyright (c) 2017.  | Volodymyr Korobko | volodymyr.korobko@gmail.com
 */

package com.korobko.automotiveapp.client.driverdetail;


import com.korobko.automotiveapp.BasePresenter;
import com.korobko.automotiveapp.BaseView;
import com.korobko.automotiveapp.server.Driver;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface DriverDetailContract {

    interface View extends BaseView<Presenter> {

        void showDriver(Driver driver);

        void showError();

        void showDriverDeleted();

        boolean isActive();
    }

    interface Presenter extends BasePresenter {

        void getDriver();

        void deleteDriver(String driverId);

    }
}
