/*
 * Copyright (c) 2017.  | Volodymyr Korobko | volodymyr.korobko@gmail.com
 */

package com.korobko.automotiveapp.client.ui.drivers;

import android.support.annotation.NonNull;


import com.korobko.automotiveapp.BasePresenter;
import com.korobko.automotiveapp.BaseView;
import com.korobko.automotiveapp.models.Driver;
import com.korobko.automotiveapp.models.RegistrationCard;

import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface DriversContract {

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showAddDriver();

        void showDriverDetailsUi(String taskId);

        void showLoadingDriversError();

        void showDrivers(List<RegistrationCard> cards);

        void showSuccessfullySavedMessage();

        boolean isActive();
    }

    interface Presenter extends BasePresenter {

        void result(int requestCode, int resultCode);

        void openDriverDetails(@NonNull RegistrationCard card);

        void addNewDriver();

        void loadDrivers();

    }
}
