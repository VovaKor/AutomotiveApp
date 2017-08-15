/*
 * Copyright (c) 2017.  | Volodymyr Korobko | volodymyr.korobko@gmail.com
 */

package com.korobko.automotiveapp.client.ui.drivers.driverdetail;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.korobko.automotiveapp.client.repository.shared.modules.InjectionModule;
import com.korobko.automotiveapp.R;
import com.korobko.automotiveapp.utils.ActivityUtils;

import static com.korobko.automotiveapp.utils.Constants.EXTRA_CARD_ID;

/**
 * Displays driver details screen.
 */
public class DriverDetailActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_driver_details);

        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);

        // Get the requested driver id
        String cardId = getIntent().getStringExtra(EXTRA_CARD_ID);

        DriverDetailFragment driverDetailFragment = (DriverDetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        if (driverDetailFragment == null) {
            driverDetailFragment = DriverDetailFragment.newInstance(cardId);

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    driverDetailFragment, R.id.contentFrame);
        }

        new DriverDetailPresenter(cardId, InjectionModule.provideRepository(getApplicationContext()),
                driverDetailFragment);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
