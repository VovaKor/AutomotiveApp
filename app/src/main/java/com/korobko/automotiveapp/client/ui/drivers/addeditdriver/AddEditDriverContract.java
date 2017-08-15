/*
 * Copyright (c) 2017.  | Volodymyr Korobko | volodymyr.korobko@gmail.com
 */

package com.korobko.automotiveapp.client.ui.drivers.addeditdriver;

import com.korobko.automotiveapp.BasePresenter;
import com.korobko.automotiveapp.BaseView;
import com.korobko.automotiveapp.models.Driver;
import com.korobko.automotiveapp.models.RegistrationCard;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface AddEditDriverContract {

    interface View extends BaseView<Presenter> {

        void showErrorEmptyField();

        void showDriversList();

        void setCard(RegistrationCard card);

        boolean isActive();

        void showInvalidEmailError();

        void showErrorEmailExist();
    }

    interface Presenter extends BasePresenter {

        void saveDriver(String id, String firstName, String lastName, String licence, String phone);

        void populateDriver();

        boolean isDataMissing();
    }
}
