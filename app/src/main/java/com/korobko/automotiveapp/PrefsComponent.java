/*
 * Copyright (c) 2017.  | Volodymyr Korobko | volodymyr.korobko@gmail.com
 */

package com.korobko.automotiveapp;

import com.korobko.automotiveapp.client.repository.shared.modules.AppModule;
import com.korobko.automotiveapp.client.repository.shared.modules.PrefsModule;
import com.korobko.automotiveapp.client.ui.cars.CarsActivity;
import com.korobko.automotiveapp.client.ui.cars.addeditcar.AddEditCarActivity;
import com.korobko.automotiveapp.client.ui.cars.cardetail.CarDetailActivity;
import com.korobko.automotiveapp.client.ui.drivers.driverdetail.DriverDetailActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by vova on 15.08.17.
 */
@Singleton
@Component(modules = { AppModule.class, PrefsModule.class })
public interface PrefsComponent {
    void inject(AutomotiveApp app);
    void inject(CarsActivity activity);
    void inject(CarDetailActivity activity);
    void inject(AddEditCarActivity activity);

    /**
     * An initializer that creates the graph from an application.
     */
    final class Initializer {
        static PrefsComponent init(AutomotiveApp app) {
            return DaggerPrefsComponent.builder()
                    .appModule(new AppModule(app))
                    .build();
        }
        private Initializer() {} // No instances.
    }
}
