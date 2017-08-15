/*
 * Copyright (c) 2017.  | Volodymyr Korobko | volodymyr.korobko@gmail.com
 */

package com.korobko.automotiveapp;

import android.support.annotation.NonNull;

import com.korobko.automotiveapp.models.RegistrationCard;

import java.util.List;

/**
 * Main entry point for accessing RegistrationCards data.
 */
public interface DataSource {

    interface LoadCardsCallback {

        void onCardsLoaded(List<RegistrationCard> registrationCards);

        void onDataNotAvailable();
    }

    interface GetCardCallback {

        void onCardLoaded(RegistrationCard registrationCard);

        void onDataNotAvailable();
    }

    interface DeleteCardCallback {

        void onSuccess();

        void onError();
    }
    interface SaveCardCallback {

        void onSuccess();

        void onError();
    }

    void getRegistrationCards(@NonNull LoadCardsCallback callback);

    void saveRegistrationCard(@NonNull RegistrationCard registrationCard, @NonNull GetCardCallback callback);

    void createRegistrationCard(@NonNull RegistrationCard registrationCard, @NonNull SaveCardCallback callback);

    void getRegistrationCard(@NonNull String registrationCardId, @NonNull GetCardCallback callback);

    void deleteRegistrationCard(@NonNull String registrationCardId, @NonNull DeleteCardCallback callback);
}
