/*
 * Copyright (c) 2017.  | Volodymyr Korobko | volodymyr.korobko@gmail.com
 */

package com.korobko.automotiveapp.models;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by vova on 13.08.17.
 * Json model for data transfer
 */

public class RegistrationCard {

    @SerializedName("rn")
    @Expose
    @NonNull
    private String registrationNumber;

    @SerializedName("driver")
    @Expose
    @NonNull
    private Driver driver;

    @SerializedName("cars")
    @Expose
    private List<Car> cars;

    public String getRegistrationNumber() {
        return this.registrationNumber;
    }
    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public Driver getDriver() {

        return this.driver;
    }

    public void setDriver(Driver driver) {
         this.driver = driver;
    }

    public List<Car> getCars() {
        return this.cars;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }

}
