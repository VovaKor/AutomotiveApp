/*
 * Copyright (c) 2017.  | Volodymyr Korobko | volodymyr.korobko@gmail.com
 */

package com.korobko.automotiveapp.client.ui.cars;

import com.korobko.automotiveapp.models.Car;

/**
 * Listens to user actions from the list item in ({@link CarsFragment}) and redirects them to the
 * Fragment's actions listener.
 */
public class CarsItemActionHandler {

    private CarsContract.Presenter mListener;

    public CarsItemActionHandler(CarsContract.Presenter listener) {
        mListener = listener;
    }

    /**
     * Called by the Data Binding library when the row is clicked.
     */
    public void carClicked(Car car) {
        mListener.openCarDetails(car);
    }
}
