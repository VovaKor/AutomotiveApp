/*
 * Copyright (c) 2017.  | Volodymyr Korobko | volodymyr.korobko@gmail.com
 */

package com.korobko.automotiveapp.client.ui.drivers.addeditdriver;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.korobko.automotiveapp.client.repository.shared.modules.InjectionModule;
import com.korobko.automotiveapp.R;
import com.korobko.automotiveapp.utils.ActivityUtils;
import com.korobko.automotiveapp.utils.Constants;

/**
 * Displays an add or edit driver screen.
 */
public class AddEditDriverActivity extends AppCompatActivity {

    private AddEditDriverPresenter mAddEditDriverPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_driver);

        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        AddEditDriverFragment addEditDriverFragment =
                (AddEditDriverFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        String cardId = getIntent().getStringExtra(Constants.EXTRA_EDIT_CARD_ID);

        if (addEditDriverFragment == null) {
            addEditDriverFragment = AddEditDriverFragment.newInstance();

            if (getIntent().hasExtra(Constants.EXTRA_EDIT_CARD_ID)) {
                actionBar.setTitle(R.string.action_bar_edit_driver);
                Bundle bundle = new Bundle();
                bundle.putString(Constants.EXTRA_EDIT_CARD_ID, cardId);
                addEditDriverFragment.setArguments(bundle);
            } else {
                actionBar.setTitle(R.string.action_bar_add_driver);
            }

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    addEditDriverFragment, R.id.contentFrame);
        }

        boolean shouldLoadDataFromRepo = true;

        // Prevent the presenter from loading data from the repository if this is a config change.
        if (savedInstanceState != null) {
            // Data might not have loaded when the config change happen, so we saved the state.
            shouldLoadDataFromRepo = savedInstanceState.getBoolean(Constants.SHOULD_LOAD_DATA_FROM_REPO_KEY);
        }

        // Create the presenter
        mAddEditDriverPresenter = new AddEditDriverPresenter(
                cardId,
                InjectionModule.provideRepository(getApplicationContext()),
                addEditDriverFragment,
                shouldLoadDataFromRepo);

        addEditDriverFragment.setPresenter(mAddEditDriverPresenter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Save the state so that next time we know if we need to refresh data.
        outState.putBoolean(Constants.SHOULD_LOAD_DATA_FROM_REPO_KEY, mAddEditDriverPresenter.isDataMissing());
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
