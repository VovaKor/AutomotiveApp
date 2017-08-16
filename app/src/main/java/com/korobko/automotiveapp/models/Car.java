/*
 * Copyright (c) 2017.  | Volodymyr Korobko | volodymyr.korobko@gmail.com
 */

package com.korobko.automotiveapp.models;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.korobko.automotiveapp.server.orm.DaoSession;
import com.korobko.automotiveapp.server.orm.CarDao;

/**
 * Created by vova on 13.08.17.
 * Model class for a Car.
 */
@Entity(active = true)
public final class Car {
    @Id
    @SerializedName("vin")
    @Expose
    @NonNull
    private String vehicleIN;
    @SerializedName("make")
    @Expose
    @NonNull
    private String make;
    @SerializedName("type")
    @Expose
    @NonNull
    private String type;
    @SerializedName("color")
    @Expose
    @NonNull
    private String color;
    @SerializedName("irc")
    @Expose
    @NonNull
    private String id_reg_card;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 709963916)
    private transient CarDao myDao;

    public Car(String vehicleIN, String make, String type, String color) {
        this.vehicleIN = vehicleIN;
        this.make = make;
        this.type = type;
        this.color = color;
    }

    @Generated(hash = 933349420)
    public Car(@NonNull String vehicleIN, @NonNull String make, @NonNull String type,
            @NonNull String color, @NonNull String id_reg_card) {
        this.vehicleIN = vehicleIN;
        this.make = make;
        this.type = type;
        this.color = color;
        this.id_reg_card = id_reg_card;
    }

    @Generated(hash = 625572433)
    public Car() {
    }

    public String getVehicleIN() {
        return vehicleIN;
    }

    public String getMake() {
        return make;
    }

    public String getType() {
        return type;
    }

    public String getColor() {
        return color;
    }

    public void setVehicleIN(String vehicleIN) {
        this.vehicleIN = vehicleIN;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getId_reg_card() {
        return this.id_reg_card;
    }

    public void setId_reg_card(String id_reg_card) {
        this.id_reg_card = id_reg_card;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 679603784)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getCarDao() : null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Car)) return false;

        Car car = (Car) o;

        return vehicleIN.equals(car.vehicleIN);

    }

    @Override
    public int hashCode() {
        return vehicleIN.hashCode();
    }
}
