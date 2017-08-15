/*
 * Copyright (c) 2017.  | Volodymyr Korobko | volodymyr.korobko@gmail.com
 */

package com.korobko.automotiveapp.client.ui.cars;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.korobko.automotiveapp.AutomotiveApp;
import com.korobko.automotiveapp.client.repository.shared.JsonPreference;
import com.korobko.automotiveapp.client.repository.shared.modules.InjectionModule;
import com.korobko.automotiveapp.R;
import com.korobko.automotiveapp.utils.ActivityUtils;

import javax.inject.Inject;

import static com.korobko.automotiveapp.utils.Constants.EXTRA_CARD_ID;

public class CarsActivity extends AppCompatActivity{

    @Inject
    JsonPreference jsonPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        AutomotiveApp.get(this).component().inject(this);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cars);
        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);

        // Get the requested driver id
        String cardId = getIntent().getStringExtra(EXTRA_CARD_ID);

        CarsFragment carsFragment =
                (CarsFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (carsFragment == null) {
            // Create the fragment
            carsFragment = CarsFragment.newInstance(cardId);
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), carsFragment, R.id.contentFrame);
        }

        // Create the presenter
        new CarsPresenter(cardId, jsonPreference, InjectionModule.provideRepository(
                getApplicationContext()), carsFragment);

        CarsViewModel carsViewModel =
                new CarsViewModel();

        carsFragment.setViewModel(carsViewModel);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
