/*
 * Copyright (c) 2017.  | Volodymyr Korobko | volodymyr.korobko@gmail.com
 */

package com.korobko.automotiveapp.client.drivers;

import android.support.annotation.NonNull;


import com.korobko.automotiveapp.BasePresenter;
import com.korobko.automotiveapp.BaseView;
import com.korobko.automotiveapp.server.Driver;

import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface DriversContract {

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showDrivers(List<Driver> drivers);

        void showAddDriver();

        void showDriverDetailsUi(String taskId);

        void showLoadingDriversError();

        void showSuccessfullySavedMessage();

        boolean isActive();
    }

    interface Presenter extends BasePresenter {

        void result(int requestCode, int resultCode);

        void addNewDriver();

        void loadDrivers();

        void openDriverDetails(@NonNull Driver requestedDriver);

    }
}
