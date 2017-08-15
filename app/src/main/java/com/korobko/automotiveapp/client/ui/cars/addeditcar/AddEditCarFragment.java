/*
 * Copyright (c) 2017.  | Volodymyr Korobko | volodymyr.korobko@gmail.com
 */

package com.korobko.automotiveapp.client.ui.cars.addeditcar;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.korobko.automotiveapp.R;
import com.korobko.automotiveapp.databinding.FragmentAddEditCarBinding;
import com.korobko.automotiveapp.databinding.FragmentAddEditDriverBinding;
import com.korobko.automotiveapp.models.Car;

/**
 * Main UI for the add/edit car screen.
 */
public class AddEditCarFragment extends Fragment implements AddEditCarContract.View {

    private AddEditCarContract.Presenter mPresenter;
    private FragmentAddEditCarBinding mViewDataBinding;
    private TextView mVIN;
    private TextView mMake;
    private TextView mType;
    private TextView mColor;


    public static AddEditCarFragment newInstance() {
        return new AddEditCarFragment();
    }

    public AddEditCarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(@NonNull AddEditCarContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.fab_add_edit_car);
        fab.setImageResource(R.drawable.ic_done);
        fab.setOnClickListener(v -> mPresenter
                .saveCar(
                        mVIN.getText().toString(),
                        mMake.getText().toString(),
                        mType.getText().toString(),
                        mColor.getText().toString()
                ));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        if (mViewDataBinding == null) {
            mViewDataBinding = FragmentAddEditCarBinding.inflate(inflater, container, false);
        }
        mVIN = mViewDataBinding.carAddVin;
        mMake = mViewDataBinding.carAddMake;
        mType = mViewDataBinding.carAddType;
        mColor = mViewDataBinding.carAddColor;

        setHasOptionsMenu(true);
        // Fragment is retained simply to persist the edits after rotation.
        setRetainInstance(true);
        return mViewDataBinding.getRoot();
    }

    @Override
    public void showErrorEmptyField() {
        Snackbar.make(mVIN, getString(R.string.error_text_edit_field_empty), Snackbar.LENGTH_LONG).show();

    }

    @Override
    public void showErrorCarExist() {
        mVIN.setError(getString(R.string.error_car_exists));
    }

    @Override
    public void showCarsList() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void showErrorLoadCar() {
        // If an error occurred, simply show an empty view.
        Snackbar.make(getView(), getString(R.string.error_car_details_loading), Snackbar.LENGTH_LONG)
                .show();
    }


    @Override
    public void setCar(Car car) {
        mViewDataBinding.setCar(car);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }
}
