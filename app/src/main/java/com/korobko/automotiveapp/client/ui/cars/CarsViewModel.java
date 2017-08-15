/*
 * Copyright (c) 2017.  | Volodymyr Korobko | volodymyr.korobko@gmail.com
 */

package com.korobko.automotiveapp.client.ui.cars;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.korobko.automotiveapp.BR;


/**
 * Exposes the data to be used in the {@link CarsContract.View}.
 * <p>
 * {@link BaseObservable} implements a listener registration mechanism which is notified when a
 * property changes. This is done by assigning a {@link Bindable} annotation to the property's
 * getter method.
 */
public class CarsViewModel extends BaseObservable {

    int mCarsListSize = 0;

    public CarsViewModel() {
    }

    @Bindable
    public boolean isNotEmpty() {
        return mCarsListSize > 0;
    }

    public void setCarsListSize(int carsListSize) {
        mCarsListSize = carsListSize;
        notifyPropertyChanged(BR.notEmpty);
    }
}
