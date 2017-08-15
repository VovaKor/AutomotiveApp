/*
 * Copyright (c) 2017.  | Volodymyr Korobko | volodymyr.korobko@gmail.com
 */

package com.korobko.automotiveapp.client.ui.cars.addeditcar;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.korobko.automotiveapp.AutomotiveApp;
import com.korobko.automotiveapp.client.repository.shared.JsonPreference;
import com.korobko.automotiveapp.client.repository.shared.modules.InjectionModule;
import com.korobko.automotiveapp.R;
import com.korobko.automotiveapp.utils.ActivityUtils;
import com.korobko.automotiveapp.utils.Constants;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

/**
 * Displays an add or edit car screen.
 */
public class AddEditCarActivity extends AppCompatActivity {

    private AddEditCarPresenter mAddEditCarPresenter;

    @Inject
    JsonPreference jsonPreference;
    private Disposable mDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        AutomotiveApp.get(this).component().inject(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_car);

        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        AddEditCarFragment addEditCarFragment =
                (AddEditCarFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        String carId = getIntent().getStringExtra(Constants.EXTRA_EDIT_CAR_ID);

        if (addEditCarFragment == null) {
            addEditCarFragment = AddEditCarFragment.newInstance();

            if (getIntent().hasExtra(Constants.EXTRA_EDIT_CAR_ID)) {
                actionBar.setTitle(R.string.action_bar_edit_car);
                Bundle bundle = new Bundle();
                bundle.putString(Constants.EXTRA_EDIT_CAR_ID, carId);
                addEditCarFragment.setArguments(bundle);
            } else {
                actionBar.setTitle(R.string.action_bar_add_car);
            }

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    addEditCarFragment, R.id.contentFrame);
        }

        boolean shouldLoadDataFromRepo = true;

        // Prevent the presenter from loading data from the repository if this is a config change.
        if (savedInstanceState != null) {
            // Data might not have loaded when the config change happen, so we saved the state.
            shouldLoadDataFromRepo = savedInstanceState.getBoolean(Constants.SHOULD_LOAD_DATA_FROM_REPO_KEY);
        }

        // Create the presenter
        mAddEditCarPresenter = new AddEditCarPresenter(
                carId,
                InjectionModule.provideRepository(getApplicationContext()),
                addEditCarFragment,
                mDisposable,
                jsonPreference,
                shouldLoadDataFromRepo);

        addEditCarFragment.setPresenter(mAddEditCarPresenter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Save the state so that next time we know if we need to refresh data.
        outState.putBoolean(Constants.SHOULD_LOAD_DATA_FROM_REPO_KEY, mAddEditCarPresenter.isDataMissing());
        super.onSaveInstanceState(outState);
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
