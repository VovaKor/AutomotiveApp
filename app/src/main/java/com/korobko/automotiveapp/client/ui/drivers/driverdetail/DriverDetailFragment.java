/*
 * Copyright (c) 2017.  | Volodymyr Korobko | volodymyr.korobko@gmail.com
 */

package com.korobko.automotiveapp.client.ui.drivers.driverdetail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.korobko.automotiveapp.R;
import com.korobko.automotiveapp.client.ui.cars.CarsActivity;
import com.korobko.automotiveapp.client.ui.drivers.addeditdriver.AddEditDriverActivity;

import com.korobko.automotiveapp.databinding.FragmentDriverDetailsBinding;
import com.korobko.automotiveapp.models.Driver;
import com.korobko.automotiveapp.utils.Constants;

/**
 * Main UI for the driver detail screen.
 */
public class DriverDetailFragment extends Fragment implements DriverDetailContract.View {

    private FragmentDriverDetailsBinding mViewDataBinding;

    private DriverDetailContract.Presenter mPresenter;

    public static DriverDetailFragment newInstance(String cardId) {
        Bundle arguments = new Bundle();
        arguments.putString(Constants.ARGUMENT_CARD_ID, cardId);
        DriverDetailFragment fragment = new DriverDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }


    @Override
    public void setPresenter(DriverDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewDataBinding.setPresenter(mPresenter);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_driver_details, container, false);

        mViewDataBinding = FragmentDriverDetailsBinding.bind(view);

        setHasOptionsMenu(true);

        setRetainInstance(true);

        // Set up floating action button
        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.fab_edit_driver);

        fab.setOnClickListener(v -> {
            String cardId = getArguments().getString(Constants.ARGUMENT_CARD_ID);
            Intent intent = new Intent(getContext(), AddEditDriverActivity.class);
            intent.putExtra(Constants.EXTRA_EDIT_CARD_ID, cardId);
            startActivityForResult(intent, Constants.REQUEST_CODE_EDIT_DRIVER);
        });

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_fragment_driver_delete:
                String cardId = getArguments().getString(Constants.ARGUMENT_CARD_ID);
                mPresenter.deleteDriver(cardId);
                return true;
        }
        return false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_driver_details, menu);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE_EDIT_DRIVER) {
            // If the driver was edited successfully, go back to the list.
            if (resultCode == Activity.RESULT_OK) {
                getActivity().finish();
                return;
            }
        }
    }

    @Override
    public void showDriver(Driver driver) {
        mViewDataBinding.setDriver(driver);
    }

    @Override
    public void showErrorLoadDriver() {
        // If an error occurred, simply show an empty task.
        Snackbar.make(getView(), getString(R.string.error_driver_details_loading), Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void showDriverDeleted() {
        getActivity().finish();
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void showErrorDeleteDriver() {
        Snackbar.make(getView(), getString(R.string.error_delete_driver), Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void showCarsUI(String cardId) {
        Intent intent = new Intent(getContext(), CarsActivity.class);
        intent.putExtra(Constants.EXTRA_CARD_ID, cardId);
        startActivity(intent);
    }
}
