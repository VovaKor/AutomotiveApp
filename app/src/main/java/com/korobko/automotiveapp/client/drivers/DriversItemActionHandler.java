/*
 * Copyright (c) 2017.  | Volodymyr Korobko | volodymyr.korobko@gmail.com
 */

package com.korobko.automotiveapp.client.drivers;

import com.korobko.automotiveapp.server.Driver;

/**
 * Listens to user actions from the list item in ({@link DriversFragment}) and redirects them to the
 * Fragment's actions listener.
 */
public class DriversItemActionHandler {

    private DriversContract.Presenter mListener;

    public DriversItemActionHandler(DriversContract.Presenter listener) {
        mListener = listener;
    }

    /**
     * Called by the Data Binding library when the row is clicked.
     */
    public void driverClicked(Driver driver) {
        mListener.openDriverDetails(driver);
    }
}
