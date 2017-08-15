/*
 * Copyright (c) 2017.  | Volodymyr Korobko | volodymyr.korobko@gmail.com
 */

package com.korobko.automotiveapp.client.ui.cars.cardetail;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.korobko.automotiveapp.AutomotiveApp;
import com.korobko.automotiveapp.client.repository.shared.JsonPreference;
import com.korobko.automotiveapp.R;
import com.korobko.automotiveapp.client.repository.shared.modules.InjectionModule;
import com.korobko.automotiveapp.utils.ActivityUtils;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

import static com.korobko.automotiveapp.utils.Constants.EXTRA_CAR_ID;

/**
 * Displays car details screen.
 */
public class CarDetailActivity extends AppCompatActivity {

    @Inject
    JsonPreference jsonPreference;

    private Disposable mDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        AutomotiveApp.get(this).component().inject(this);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_car_details);

        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);

        // Get the requested car id
        String carId = getIntent().getStringExtra(EXTRA_CAR_ID);

        CarDetailFragment carDetailFragment = (CarDetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        if (carDetailFragment == null) {
            carDetailFragment = CarDetailFragment.newInstance(carId);

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    carDetailFragment, R.id.contentFrame);
        }

        new CarDetailPresenter(carId,
                jsonPreference,
                mDisposable,
                InjectionModule.provideRepository(getApplicationContext()),
                carDetailFragment);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (mDisposable!=null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }
}
