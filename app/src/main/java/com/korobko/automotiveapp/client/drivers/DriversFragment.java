/*
 * Copyright (c) 2017.  | Volodymyr Korobko | volodymyr.korobko@gmail.com
 */

package com.korobko.automotiveapp.client.drivers;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.korobko.automotiveapp.R;
import com.korobko.automotiveapp.client.addeditdriver.AddEditDriverActivity;
import com.korobko.automotiveapp.client.driverdetail.DriverDetailActivity;

import com.korobko.automotiveapp.databinding.FragmentDriversBinding;
import com.korobko.automotiveapp.databinding.ListViewItemDriversBinding;
import com.korobko.automotiveapp.restapi.Driver;
import com.korobko.automotiveapp.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vova on 10.08.17.
 * Display a grid of {@link Driver}s.
 */
public class DriversFragment extends Fragment implements DriversContract.View {

    private DriversContract.Presenter mPresenter;

    private DriversAdapter mListAdapter;

    private DriversViewModel mDriversViewModel;

    public DriversFragment() {
        // Requires empty public constructor
    }

    public static DriversFragment newInstance() {
        return new DriversFragment();
    }

    @Override
    public void setPresenter(@NonNull DriversContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPresenter.result(requestCode, resultCode);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentDriversBinding fragmentDriversBinding = FragmentDriversBinding.inflate(inflater, container, false);

        fragmentDriversBinding.setDrivers(mDriversViewModel);

        fragmentDriversBinding.setActionHandler(mPresenter);

        // Set up drivers view
        ListView listView = fragmentDriversBinding.listViewDrivers;

        mListAdapter = new DriversAdapter(new ArrayList<Driver>(0), mPresenter);
        listView.setAdapter(mListAdapter);

        // Set up floating action button
        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.fab_add_driver);

        fab.setImageResource(R.drawable.ic_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.addNewDriver();
            }
        });

        // Set up progress indicator
        final ScrollChildSwipeRefreshLayout swipeRefreshLayout = fragmentDriversBinding.refreshLayout;
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
        );
        // Set the scrolling view in the custom SwipeRefreshLayout.
        swipeRefreshLayout.setScrollUpChild(listView);

        setHasOptionsMenu(true);

        return fragmentDriversBinding.getRoot();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh_drivers:
                mPresenter.loadDrivers();
                break;
        }
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_drivers, menu);
    }

    public void setViewModel(DriversViewModel viewModel) {
        mDriversViewModel = viewModel;
    }

    @Override
    public void setLoadingIndicator(final boolean active) {

        if (getView() == null) {
            return;
        }
        final SwipeRefreshLayout srl =
                (SwipeRefreshLayout) getView().findViewById(R.id.refresh_layout);

        // Make sure setRefreshing() is called after the layout is done with everything else.
        srl.post(new Runnable() {
            @Override
            public void run() {
                srl.setRefreshing(active);
            }
        });
    }

    @Override
    public void showDrivers(List<Driver> drivers) {
        mListAdapter.replaceData(drivers);
        mDriversViewModel.setDriverListSize(drivers.size());
    }

    @Override
    public void showSuccessfullySavedMessage() {
        showMessage(getString(R.string.success_driver_saved));
    }

    @Override
    public void showAddDriver() {
        Intent intent = new Intent(getContext(), AddEditDriverActivity.class);
        startActivityForResult(intent, Constants.REQUEST_CODE_ADD_DRIVER);
    }

    @Override
    public void showDriverDetailsUi(String driverId) {
        // in it's own Activity, since it makes more sense that way and it gives us the flexibility
        // to show some Intent stubbing.
        Intent intent = new Intent(getContext(), DriverDetailActivity.class);
        intent.putExtra(Constants.EXTRA_DRIVER_ID, driverId);
        startActivity(intent);
    }


    @Override
    public void showLoadingDriversError() {
        showMessage(getString(R.string.error_drivers_loading));
    }

    private void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    private static class DriversAdapter extends BaseAdapter {

        private List<Driver> mDrivers;

        private DriversContract.Presenter mUserActionsListener;

        public DriversAdapter(List<Driver> drivers, DriversContract.Presenter itemListener) {
            setList(drivers);
            mUserActionsListener = itemListener;
        }

        public void replaceData(List<Driver> drivers) {
            setList(drivers);
        }

        private void setList(List<Driver> drivers) {
            mDrivers = drivers;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mDrivers != null ? mDrivers.size() : 0;
        }

        @Override
        public Driver getItem(int i) {
            return mDrivers.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            Driver driver = getItem(i);
            ListViewItemDriversBinding binding;
            if (view == null) {
                // Inflate
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

                // Create the binding
                binding = ListViewItemDriversBinding.inflate(inflater, viewGroup, false);
            } else {
                binding = DataBindingUtil.getBinding(view);
            }

            // We might be recycling the binding for another driver, so update it.
            // Create the action handler for the view
            DriversItemActionHandler itemActionHandler =
                    new DriversItemActionHandler (mUserActionsListener);
            binding.setActionHandler(itemActionHandler);
            binding.setDriver(driver);
            binding.executePendingBindings();
            return binding.getRoot();
        }
    }
}

