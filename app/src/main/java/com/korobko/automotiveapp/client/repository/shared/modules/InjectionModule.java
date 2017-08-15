/*
 * Copyright (c) 2017.  | Volodymyr Korobko | volodymyr.korobko@gmail.com
 */

package com.korobko.automotiveapp.client.repository.shared.modules;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.korobko.automotiveapp.client.repository.RemoteDataSource;
import com.korobko.automotiveapp.client.repository.Repository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by vova on 14.08.17.
 */

@Module
public class InjectionModule {

    /**
     * Enables injection of production implementations for
     * {@link RemoteDataSource} at compile time.
     */
    public static Repository provideRepository(@NonNull Context context) {

        return Repository.getInstance(RemoteDataSource.getInstance());
    }

    // Dagger will only look for methods annotated with @Provides
    // Application reference must come from AppModule.class
    @Provides
    @Singleton
    public SharedPreferences providesSharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }
}