/*
 * Copyright (c) 2017.  | Volodymyr Korobko | volodymyr.korobko@gmail.com
 */

package com.korobko.automotiveapp.client.ui.cars.cardetail;

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
import com.korobko.automotiveapp.client.ui.cars.addeditcar.AddEditCarActivity;
import com.korobko.automotiveapp.databinding.FragmentCarDetailsBinding;
import com.korobko.automotiveapp.models.Car;
import com.korobko.automotiveapp.utils.Constants;

/**
 * Main UI for the car detail screen.
 */
public class CarDetailFragment extends Fragment implements CarDetailContract.View {

    private FragmentCarDetailsBinding mViewDataBinding;

    private CarDetailContract.Presenter mPresenter;

    public static CarDetailFragment newInstance(String carId) {
        Bundle arguments = new Bundle();
        arguments.putString(Constants.ARGUMENT_CAR_ID, carId);
        CarDetailFragment fragment = new CarDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }


    @Override
    public void setPresenter(CarDetailContract.Presenter presenter) {
        mPresenter = presenter;
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

        View view = inflater.inflate(R.layout.fragment_car_details, container, false);

        mViewDataBinding = FragmentCarDetailsBinding.bind(view);

        setHasOptionsMenu(true);

        setRetainInstance(true);

        // Set up floating action button
        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.fab_edit_car);

        fab.setOnClickListener(v -> {
            String carId = getArguments().getString(Constants.ARGUMENT_CAR_ID);
            Intent intent = new Intent(getContext(), AddEditCarActivity.class);
            intent.putExtra(Constants.EXTRA_EDIT_CAR_ID, carId);
            startActivityForResult(intent, Constants.REQUEST_CODE_EDIT_CAR);
        });

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_fragment_car_delete:
                String carId = getArguments().getString(Constants.ARGUMENT_CAR_ID);
                mPresenter.deleteCar(carId);
                return true;
        }
        return false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_car_details, menu);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE_EDIT_CAR) {
            // If the car was edited successfully, go back to the list.
            if (resultCode == Activity.RESULT_OK) {
                getActivity().finish();
                return;
            }
        }
    }

    @Override
    public void showCar(Car car) {
        mViewDataBinding.setCar(car);
    }

    @Override
    public void showErrorLoadCar() {
        // If an error occurred, simply show an empty view.
        Snackbar.make(getView(), getString(R.string.error_car_details_loading), Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void showCarDeleted() {
        getActivity().finish();
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void showErrorDeleteCar() {
        Snackbar.make(getView(), getString(R.string.error_delete_car), Snackbar.LENGTH_LONG)
                .show();
    }

}
