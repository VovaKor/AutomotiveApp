/*
 * Copyright (c) 2017.  | Volodymyr Korobko | volodymyr.korobko@gmail.com
 */

package com.korobko.automotiveapp.restapi;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.korobko.automotiveapp.server.orm.DaoSession;
import com.korobko.automotiveapp.server.orm.CarDao;
import com.korobko.automotiveapp.server.orm.DriverDao;
import com.korobko.automotiveapp.server.orm.RegistrationCardDao;

/**
 * Created by vova on 13.08.17.
 * General model for data transfer
 */
@Entity(active = true)
public class RegistrationCard {
    @Id
    @SerializedName("rn")
    @Expose
    @NonNull
    private String registrationNumber;
    private String id_driver;
    @ToOne(joinProperty = "id_driver")
    @SerializedName("driver")
    @Expose
    @NonNull
    private Driver driver;
    @ToMany(referencedJoinProperty = "id_reg_card")
    @SerializedName("cars")
    @Expose
    private List<Car> cars;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1047696480)
    private transient RegistrationCardDao myDao;
    @Generated(hash = 1161723661)
    public RegistrationCard(@NonNull String registrationNumber, String id_driver) {
        this.registrationNumber = registrationNumber;
        this.id_driver = id_driver;
    }
    @Generated(hash = 604497147)
    public RegistrationCard() {
    }
    public String getRegistrationNumber() {
        return this.registrationNumber;
    }
    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }
    public String getId_driver() {
        return this.id_driver;
    }
    public void setId_driver(String id_driver) {
        this.id_driver = id_driver;
    }
    @Generated(hash = 92741137)
    private transient String driver__resolvedKey;
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1870379185)
    public Driver getDriver() {
        String __key = this.id_driver;
        if (driver__resolvedKey == null || driver__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            DriverDao targetDao = daoSession.getDriverDao();
            Driver driverNew = targetDao.load(__key);
            synchronized (this) {
                driver = driverNew;
                driver__resolvedKey = __key;
            }
        }
        return driver;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 409973132)
    public void setDriver(Driver driver) {
        synchronized (this) {
            this.driver = driver;
            id_driver = driver == null ? null : driver.getId();
            driver__resolvedKey = id_driver;
        }
    }
    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1905947431)
    public List<Car> getCars() {
        if (cars == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            CarDao targetDao = daoSession.getCarDao();
            List<Car> carsNew = targetDao
                    ._queryRegistrationCard_Cars(registrationNumber);
            synchronized (this) {
                if (cars == null) {
                    cars = carsNew;
                }
            }
        }
        return cars;
    }
    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1055213807)
    public synchronized void resetCars() {
        cars = null;
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
    @Generated(hash = 2058467067)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getRegistrationCardDao() : null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RegistrationCard)) return false;

        RegistrationCard that = (RegistrationCard) o;

        if (!registrationNumber.equals(that.registrationNumber)) return false;
        if (id_driver != null ? !id_driver.equals(that.id_driver) : that.id_driver != null)
            return false;
        if (!driver.equals(that.driver)) return false;
        return cars != null ? cars.equals(that.cars) : that.cars == null;

    }

    @Override
    public int hashCode() {
        int result = registrationNumber.hashCode();
        result = 31 * result + (id_driver != null ? id_driver.hashCode() : 0);
        result = 31 * result + driver.hashCode();
        result = 31 * result + (cars != null ? cars.hashCode() : 0);
        return result;
    }
}
