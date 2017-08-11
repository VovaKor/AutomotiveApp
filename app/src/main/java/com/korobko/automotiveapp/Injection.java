/*
 * Copyright (c) 2017.  | Volodymyr Korobko | volodymyr.korobko@gmail.com
 */

package com.korobko.automotiveapp;

import android.content.Context;
import android.support.annotation.NonNull;

import com.korobko.automotiveapp.client.repository.DriversDataSource;
import com.korobko.automotiveapp.client.repository.DriversRemoteDataSource;
import com.korobko.automotiveapp.client.repository.DriversRepository;

/**
 * Enables injection of production implementations for
 * {@link DriversDataSource} at compile time.
 */
public class Injection {

    public static DriversRepository provideDriversRepository(@NonNull Context context) {

        return DriversRepository.getInstance(DriversRemoteDataSource.getInstance());
    }
}