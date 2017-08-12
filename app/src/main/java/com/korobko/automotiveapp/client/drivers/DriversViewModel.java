/*
 * Copyright (c) 2017.  | Volodymyr Korobko | volodymyr.korobko@gmail.com
 */

package com.korobko.automotiveapp.client.drivers;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.korobko.automotiveapp.BR;


/**
 * Exposes the data to be used in the {@link DriversContract.View}.
 * <p>
 * {@link BaseObservable} implements a listener registration mechanism which is notified when a
 * property changes. This is done by assigning a {@link Bindable} annotation to the property's
 * getter method.
 */
public class DriversViewModel extends BaseObservable {

    int mDriverListSize = 0;

    private final DriversContract.Presenter mPresenter;

    private Context mContext;

    public DriversViewModel(Context context, DriversContract.Presenter presenter) {
        mContext = context;
        mPresenter = presenter;
    }

    @Bindable
    public boolean isNotEmpty() {
        return mDriverListSize > 0;
    }

    public void setDriverListSize(int driverListSize) {
        mDriverListSize = driverListSize;
        notifyPropertyChanged(BR.notEmpty);
    }
}
