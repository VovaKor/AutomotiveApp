/*
 * Copyright (c) 2017.  | Volodymyr Korobko | volodymyr.korobko@gmail.com
 */

package com.korobko.automotiveapp.client.drivers;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.korobko.automotiveapp.Injection;
import com.korobko.automotiveapp.R;
import com.korobko.automotiveapp.server.AMService;
import com.korobko.automotiveapp.utils.ActivityUtils;

public class DriversActivity extends AppCompatActivity{
    private DrawerLayout mDrawerLayout;

    private DriversPresenter mDriversPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startService(new Intent(this, AMService.class));

        setContentView(R.layout.activity_drivers);
        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        // Set up the navigation drawer.
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        DriversFragment driversFragment =
                (DriversFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (driversFragment == null) {
            // Create the fragment
            driversFragment = DriversFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), driversFragment, R.id.contentFrame);
        }

        // Create the presenter
        mDriversPresenter = new DriversPresenter(Injection.provideDriversRepository(
                getApplicationContext()), driversFragment);

        DriversViewModel driversViewModel =
                new DriversViewModel(getApplicationContext(), mDriversPresenter);

        driversFragment.setViewModel(driversViewModel);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Open the navigation drawer when the home icon is selected from the toolbar.
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    switch (menuItem.getItemId()) {
                        case R.id.navigation_menu_driver_list:
                            // Do nothing, we're already on that screen
                            break;
                        default:
                            break;
                    }
                    // Close the navigation drawer when an item is selected.
                    menuItem.setChecked(true);
                    mDrawerLayout.closeDrawers();
                    return true;
                });
    }
//

}
