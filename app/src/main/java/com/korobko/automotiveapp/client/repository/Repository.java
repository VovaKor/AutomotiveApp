/*
 * Copyright (c) 2017.  | Volodymyr Korobko | volodymyr.korobko@gmail.com
 */

package com.korobko.automotiveapp.client.repository;

import android.support.annotation.NonNull;

import com.korobko.automotiveapp.DataSource;
import com.korobko.automotiveapp.models.RegistrationCard;

import java.util.List;

/**
 * Concrete implementation to load drivers from the data source.
 */
public class Repository implements DataSource {

    private static Repository INSTANCE = null;

    private final DataSource mRemoteDataSource;


    // Prevent direct instantiation.
    private Repository(@NonNull DataSource remoteDataSource) {
        mRemoteDataSource = remoteDataSource;

    }

    /**
     * Returns the single instance of this class, creating it if necessary.
     * @param remoteDataSource the backend data source
     * @return the {@link Repository} instance
     */
    public static Repository getInstance(DataSource remoteDataSource
                                                ) {
        if (INSTANCE == null) {
            INSTANCE = new Repository(remoteDataSource);
        }
        return INSTANCE;
    }


    /**
     * Gets all drivers from remote data source.
     */
    @Override
    public void getRegistrationCards(@NonNull final LoadCardsCallback callback) {
        mRemoteDataSource.getRegistrationCards(new LoadCardsCallback() {
            @Override
            public void onCardsLoaded(List<RegistrationCard> cards) {

                callback.onCardsLoaded(cards);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void saveRegistrationCard(@NonNull RegistrationCard registrationCard, @NonNull GetCardCallback callback) {

        mRemoteDataSource.saveRegistrationCard(registrationCard, new GetCardCallback() {
            @Override
            public void onCardLoaded(RegistrationCard registrationCard) {
                callback.onCardLoaded(registrationCard);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });


    }

    @Override
    public void createRegistrationCard(@NonNull RegistrationCard registrationCard, @NonNull SaveCardCallback callback) {
        mRemoteDataSource.createRegistrationCard(registrationCard, new SaveCardCallback() {
            @Override
            public void onSuccess() {
                callback.onSuccess();
            }

            @Override
            public void onError() {
                callback.onError();
            }
        });
    }

    /**
     * Gets driver with {@param registrationCardId} from remote data source.
     */
    @Override
    public void getRegistrationCard(@NonNull final String registrationCardId, @NonNull final GetCardCallback callback) {
        mRemoteDataSource.getRegistrationCard(registrationCardId, new GetCardCallback() {
            @Override
            public void onCardLoaded(RegistrationCard registrationCard) {

                callback.onCardLoaded(registrationCard);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void deleteRegistrationCard(@NonNull String registrationCardId, @NonNull final DeleteCardCallback callback) {
        mRemoteDataSource.deleteRegistrationCard(registrationCardId, new DeleteCardCallback() {
            @Override
            public void onSuccess() {
                callback.onSuccess();
            }

            @Override
            public void onError() {
                callback.onError();
            }
        });

    }

}
