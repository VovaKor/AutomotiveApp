/*
 * Copyright (c) 2017.  | Volodymyr Korobko | volodymyr.korobko@gmail.com
 */

package com.korobko.automotiveapp.client.repository;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.korobko.automotiveapp.AutomotiveApp;
import com.korobko.automotiveapp.restapi.Driver;
import com.korobko.automotiveapp.utils.Constants;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Implementation of the remote data source.
 */
public class DriversRemoteDataSource implements DriversDataSource {

    private static DriversRemoteDataSource INSTANCE;
    private CompositeDisposable mCompositeDisposable;

    public static DriversRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DriversRemoteDataSource();
        }
        return INSTANCE;
    }

    // Prevent direct instantiation.
    private DriversRemoteDataSource() {
        mCompositeDisposable = new CompositeDisposable();
    }


    @Override
    public void createDriver(Driver driver, SaveDriverCallback callback) {

         mCompositeDisposable.add(AutomotiveApp.getAutomotiveAPI()
                        .createDriver(driver)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<ResponseBody>() {
                            @Override
                            public void onSuccess(ResponseBody value) {
                                callback.onSuccess();
                            }

                            @Override
                            public void onError(Throwable e) {

                                callback.onError();
                            }
                        })
                );
    }

    @Override
    public void getDrivers(final @NonNull LoadDriversCallback callback) {
        mCompositeDisposable.add(AutomotiveApp.getAutomotiveAPI()
                .getDrivers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<Driver>>() {
                    @Override
                    public void onSuccess(List<Driver> value) {
                        callback.onDriversLoaded(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onDataNotAvailable();
                    }
                })
        );
    }


    @Override
    public void getDriver(@NonNull String driverId, final @NonNull GetDriverCallback callback) {
        mCompositeDisposable.add(AutomotiveApp.getAutomotiveAPI()
                .getDriverById(driverId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Driver>() {
                    @Override
                    public void onSuccess(Driver value) {
                        callback.onDriverLoaded(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onDataNotAvailable();
                    }
                })
        );
    }

    @Override
    public void saveDriver(@NonNull Driver driver, SaveDriverCallback callback) {
        mCompositeDisposable.add(AutomotiveApp.getAutomotiveAPI()
                .updateDriver(driver)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<ResponseBody>() {
                    @Override
                    public void onSuccess(ResponseBody value) {
                        callback.onSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError();
                    }
                })
        );
    }

    @Override
    public void deleteDriver(@NonNull String driverId, @NonNull DeleteDriverCallback callback) {
        mCompositeDisposable.add(AutomotiveApp.getAutomotiveAPI()
                .deleteDriverById(driverId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Driver>() {
                    @Override
                    public void onSuccess(Driver value) {
                        callback.onSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError();
                    }
                })
        );
    }
}
