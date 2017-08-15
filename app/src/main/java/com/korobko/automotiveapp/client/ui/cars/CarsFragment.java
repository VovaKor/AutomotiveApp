/*
 * Copyright (c) 2017.  | Volodymyr Korobko | volodymyr.korobko@gmail.com
 */

package com.korobko.automotiveapp.client.ui.cars;

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
import com.korobko.automotiveapp.ScrollChildSwipeRefreshLayout;
import com.korobko.automotiveapp.client.ui.cars.addeditcar.AddEditCarActivity;
import com.korobko.automotiveapp.client.ui.cars.cardetail.CarDetailActivity;
import com.korobko.automotiveapp.databinding.FragmentCarsBinding;
import com.korobko.automotiveapp.databinding.ListViewItemCarsBinding;
import com.korobko.automotiveapp.models.Car;
import com.korobko.automotiveapp.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vova on 10.08.17.
 * Display a grid of {@link Car}s.
 */
public class CarsFragment extends Fragment implements CarsContract.View {

    private CarsContract.Presenter mPresenter;

    private CarsAdapter mListAdapter;

    private CarsViewModel mCarsViewModel;

    public static CarsFragment newInstance(String cardId) {
        Bundle arguments = new Bundle();
        arguments.putString(Constants.ARGUMENT_CARD_ID, cardId);
        CarsFragment fragment = new CarsFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void setPresenter(@NonNull CarsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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
        FragmentCarsBinding fragmentCarsBinding = FragmentCarsBinding.inflate(inflater, container, false);

        fragmentCarsBinding.setCars(mCarsViewModel);

        fragmentCarsBinding.setActionHandler(mPresenter);

        // Set up drivers view
        ListView listView = fragmentCarsBinding.listViewCars;

        mListAdapter = new CarsAdapter(new ArrayList<Car>(0), mPresenter);
        listView.setAdapter(mListAdapter);

        // Set up floating action button
        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.fab_add_car);

        fab.setImageResource(R.drawable.ic_add);
        fab.setOnClickListener(v -> mPresenter.addNewCar());

        // Set up progress indicator
        final ScrollChildSwipeRefreshLayout swipeRefreshLayout = fragmentCarsBinding.refreshLayout;
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
        );
        // Set the scrolling view in the custom SwipeRefreshLayout.
        swipeRefreshLayout.setScrollUpChild(listView);

        setHasOptionsMenu(true);

        return fragmentCarsBinding.getRoot();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh_cars:
                mPresenter.loadCars();
                break;
        }
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_cars, menu);
    }

    public void setViewModel(CarsViewModel viewModel) {
        mCarsViewModel = viewModel;
    }

    @Override
    public void setLoadingIndicator(final boolean active) {

        if (getView() == null) {
            return;
        }
        final SwipeRefreshLayout srl =
                (SwipeRefreshLayout) getView().findViewById(R.id.refresh_layout);

        // Make sure setRefreshing() is called after the layout is done with everything else.
        srl.post(() -> srl.setRefreshing(active));
    }

    @Override
    public void showCars(List<Car> cars) {
        mListAdapter.replaceData(cars);
        mCarsViewModel.setCarsListSize(cars.size());
    }

    @Override
    public void showSuccessfullySavedMessage() {
        showMessage(getString(R.string.success_car_saved));
    }

    @Override
    public void showAddCar() {
        Intent intent = new Intent(getContext(), AddEditCarActivity.class);
        startActivityForResult(intent, Constants.REQUEST_CODE_ADD_CAR);
    }

    @Override
    public void showCarDetailsUi(String carId) {
        // in it's own Activity, since it makes more sense that way and it gives us the flexibility
        // to show some Intent stubbing.
        Intent intent = new Intent(getContext(), CarDetailActivity.class);
        intent.putExtra(Constants.EXTRA_CAR_ID, carId);
        startActivity(intent);
    }


    @Override
    public void showLoadingCarsError() {
        showMessage(getString(R.string.error_cars_loading));
    }

    private void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    private static class CarsAdapter extends BaseAdapter {

        private List<Car> mCars;

        private CarsContract.Presenter mUserActionsListener;

        public CarsAdapter(ArrayList<Car> cars, CarsContract.Presenter itemListener) {
            setList(cars);
            mUserActionsListener = itemListener;
        }

        public void replaceData(List<Car> cars) {
            setList(cars);
        }

        private void setList(List<Car> cars) {
            mCars = cars;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mCars != null ? mCars.size() : 0;
        }

        @Override
        public Car getItem(int i) {
            return mCars.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            Car car = getItem(i);
            ListViewItemCarsBinding binding;
            if (view == null) {
                // Inflate
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

                // Create the binding
                binding = ListViewItemCarsBinding.inflate(inflater, viewGroup, false);
            } else {
                binding = DataBindingUtil.getBinding(view);
            }

            // We might be recycling the binding for another Car, so update it.
            // Create the action handler for the view
            CarsItemActionHandler itemActionHandler =
                    new CarsItemActionHandler(mUserActionsListener);
            binding.setActionHandler(itemActionHandler);
            binding.setCar(car);
            binding.executePendingBindings();
            return binding.getRoot();
        }
    }
}

