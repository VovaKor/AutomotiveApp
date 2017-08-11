/*
 * Copyright (c) 2017.  | Volodymyr Korobko | volodymyr.korobko@gmail.com
 */

package com.korobko.automotiveapp.client.addeditdriver;

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
import com.korobko.automotiveapp.databinding.FragmentAddEditDriverBinding;
import com.korobko.automotiveapp.server.Driver;

/**
 * Main UI for the add/edit driver screen.
 */
public class AddEditDriverFragment extends Fragment implements AddEditDriverContract.View {

    private AddEditDriverContract.Presenter mPresenter;
    private FragmentAddEditDriverBinding mViewDataBinding;
    private TextView mEmail;
    private TextView mFirstName;
    private TextView mLastName;
    private TextView mLicence;
    private TextView mPhone;

    public static AddEditDriverFragment newInstance() {
        return new AddEditDriverFragment();
    }

    public AddEditDriverFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(@NonNull AddEditDriverContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.fab_add_edit_driver);
        fab.setImageResource(R.drawable.ic_done);
        fab.setOnClickListener(v -> mPresenter
                .saveDriver(
                        mEmail.getText().toString(),
                        mFirstName.getText().toString(),
                        mLastName.getText().toString(),
                        mLicence.getText().toString(),
                        mPhone.getText().toString()
                ));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        if (mViewDataBinding == null) {
            mViewDataBinding = FragmentAddEditDriverBinding.inflate(inflater, container, false);
        }
        mEmail = mViewDataBinding.driverAddEmail;
        mFirstName = mViewDataBinding.driverAddFirstName;
        mLastName = mViewDataBinding.driverAddLastName;
        mLicence = mViewDataBinding.driverAddLicence;
        mPhone = mViewDataBinding.driverAddPhone;

        setHasOptionsMenu(true);
        // Fragment is retained simply to persist the edits after rotation.
        setRetainInstance(true);
        return mViewDataBinding.getRoot();
    }

    @Override
    public void showErrorEmptyField() {
        Snackbar.make(mEmail, getString(R.string.error_text_edit_field_empty), Snackbar.LENGTH_LONG).show();

    }
    @Override
    public void showInvalidEmailError() {
        mEmail.setError(getString(R.string.error_email_invalid));
    }

    @Override
    public void showErrorEmailExist() {
        mEmail.setError(getString(R.string.error_email_exists));
    }

    @Override
    public void showDriversList() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void setDriver(Driver driver) {
        mViewDataBinding.setDriver(driver);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }
}
