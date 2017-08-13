/*
 * Copyright (c) 2017.  | Volodymyr Korobko | volodymyr.korobko@gmail.com
 */

package com.korobko.automotiveapp.server.orm;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.korobko.automotiveapp.restapi.RegistrationCard;
import com.korobko.automotiveapp.restapi.Car;
import com.korobko.automotiveapp.restapi.Driver;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig registrationCardDaoConfig;
    private final DaoConfig carDaoConfig;
    private final DaoConfig driverDaoConfig;

    private final RegistrationCardDao registrationCardDao;
    private final CarDao carDao;
    private final DriverDao driverDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        registrationCardDaoConfig = daoConfigMap.get(RegistrationCardDao.class).clone();
        registrationCardDaoConfig.initIdentityScope(type);

        carDaoConfig = daoConfigMap.get(CarDao.class).clone();
        carDaoConfig.initIdentityScope(type);

        driverDaoConfig = daoConfigMap.get(DriverDao.class).clone();
        driverDaoConfig.initIdentityScope(type);

        registrationCardDao = new RegistrationCardDao(registrationCardDaoConfig, this);
        carDao = new CarDao(carDaoConfig, this);
        driverDao = new DriverDao(driverDaoConfig, this);

        registerDao(RegistrationCard.class, registrationCardDao);
        registerDao(Car.class, carDao);
        registerDao(Driver.class, driverDao);
    }
    
    public void clear() {
        registrationCardDaoConfig.clearIdentityScope();
        carDaoConfig.clearIdentityScope();
        driverDaoConfig.clearIdentityScope();
    }

    public RegistrationCardDao getRegistrationCardDao() {
        return registrationCardDao;
    }

    public CarDao getCarDao() {
        return carDao;
    }

    public DriverDao getDriverDao() {
        return driverDao;
    }

}
