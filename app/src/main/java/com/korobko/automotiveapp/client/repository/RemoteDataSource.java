/*
 * Copyright (c) 2017.  | Volodymyr Korobko | volodymyr.korobko@gmail.com
 */

package com.korobko.automotiveapp.client.repository;

import android.support.annotation.NonNull;

import com.korobko.automotiveapp.AutomotiveApp;
import com.korobko.automotiveapp.DataSource;
import com.korobko.automotiveapp.models.RegistrationCard;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Implementation of the remote data source.
 */
public class RemoteDataSource implements DataSource {

    private static RemoteDataSource INSTANCE;
    private CompositeDisposable mCompositeDisposable;

    public static RemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RemoteDataSource();
        }
        return INSTANCE;
    }

    // Prevent direct instantiation.
    private RemoteDataSource() {
        mCompositeDisposable = new CompositeDisposable();
    }


    @Override
    public void createRegistrationCard(@NonNull RegistrationCard registrationCard, @NonNull SaveCardCallback callback) {

         mCompositeDisposable.add(AutomotiveApp.getAutomotiveAPI()
                        .createRegistrationCard(registrationCard)
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
    public void getRegistrationCards(final @NonNull LoadCardsCallback callback) {
        mCompositeDisposable.add(AutomotiveApp.getAutomotiveAPI()
                .getRegistrationCards()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<RegistrationCard>>() {
                    @Override
                    public void onSuccess(List<RegistrationCard> value) {
                        callback.onCardsLoaded(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onDataNotAvailable();
                    }
                })
        );
    }


    @Override
    public void getRegistrationCard(@NonNull String registrationCardId, final @NonNull GetCardCallback callback) {
        mCompositeDisposable.add(AutomotiveApp.getAutomotiveAPI()
                .getRegistrationCardById(registrationCardId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<RegistrationCard>() {
                    @Override
                    public void onSuccess(RegistrationCard value) {
                        callback.onCardLoaded(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onDataNotAvailable();
                    }
                })
        );
    }

    @Override
    public void saveRegistrationCard(@NonNull RegistrationCard registrationCard, @NonNull GetCardCallback callback) {
        mCompositeDisposable.add(AutomotiveApp.getAutomotiveAPI()
                .updateRegistrationCard(registrationCard)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<RegistrationCard>() {
                    @Override
                    public void onSuccess(RegistrationCard value) {

                        callback.onCardLoaded(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onDataNotAvailable();
                    }
                })
        );
    }

    @Override
    public void deleteRegistrationCard(@NonNull String registrationCardId, @NonNull DeleteCardCallback callback) {
        mCompositeDisposable.add(AutomotiveApp.getAutomotiveAPI()
                .deleteRegistrationCardById(registrationCardId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<RegistrationCard>() {
                    @Override
                    public void onSuccess(RegistrationCard value) {
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
