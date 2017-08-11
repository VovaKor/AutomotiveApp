/*
 * Copyright (c) 2017.  | Volodymyr Korobko | volodymyr.korobko@gmail.com
 */

package com.korobko.automotiveapp.server;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

/**
 * Immutable model class for a Driver.
 */
public final class Driver {

    @NonNull
    private final String id;
    @NonNull
    private final String firstName;
    @NonNull
    private final String lastName;
    @NonNull
    private final String phone;
    @NonNull
    private final String licenceNumber;

    public Driver(@NonNull String id, @NonNull String firstName, @NonNull String lastName, @NonNull String phone, @NonNull String licenceNumber) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.licenceNumber = licenceNumber;

    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getFirstName() {
        return firstName;
    }

    @NonNull
    public String getLastName() {
        return lastName;
    }

    @NonNull
    public String getPhone() {
        return phone;
    }

    @NonNull
    public String getLicenceNumber() {
        return licenceNumber;
    }

}
