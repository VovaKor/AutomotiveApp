/*
 * Copyright (c) 2017.  | Volodymyr Korobko | volodymyr.korobko@gmail.com
 */

package com.korobko.automotiveapp.server.repository;

import android.content.Context;
import android.support.annotation.NonNull;

import com.korobko.automotiveapp.DataSource;
import com.korobko.automotiveapp.models.Driver;
import com.korobko.automotiveapp.models.RegistrationCard;
import com.korobko.automotiveapp.server.orm.DaoMaster;
import com.korobko.automotiveapp.server.orm.DaoSession;
import com.korobko.automotiveapp.server.orm.RegistrationCardDao;

import org.greenrobot.greendao.database.Database;

import java.util.ArrayList;
import java.util.List;

import static com.korobko.automotiveapp.utils.Constants.DATABASE_NAME;

/**
 * Implementation of the local data source.
 */
public class LocalDataSource implements DataSource {

    private static LocalDataSource INSTANCE;
    private DaoSession mDaoSession;

    public static LocalDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new LocalDataSource(context);
        }
        return INSTANCE;
    }

    // Prevent direct instantiation.
    private LocalDataSource(@NonNull Context context) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, DATABASE_NAME);
        Database db = helper.getWritableDb();
        mDaoSession = new DaoMaster(db).newSession();
        bootstrapDB();
    }

    @Override
    public void createRegistrationCard(@NonNull RegistrationCard registrationCard, @NonNull SaveCardCallback callback) {
        RegistrationCardDao cardDao = mDaoSession.getRegistrationCardDao();
        cardDao.insert(registrationCard);
        RegistrationCard card = cardDao.load(registrationCard.getRegistrationNumber());
        if (card!=null){
            callback.onSuccess();
        }else {
            callback.onError();
        }

    }

    @Override
    public void getRegistrationCards(final @NonNull LoadCardsCallback callback) {
        RegistrationCardDao cardDao = mDaoSession.getRegistrationCardDao();
        List<RegistrationCard> cards = cardDao.loadAll();
        if (cards.isEmpty()){
            callback.onCardsLoaded(cards);
        }else {
            callback.onDataNotAvailable();
        }
    }


    @Override
    public void getRegistrationCard(@NonNull String registrationCardId, final @NonNull GetCardCallback callback) {
        RegistrationCardDao cardDao = mDaoSession.getRegistrationCardDao();
        RegistrationCard card = cardDao.load(registrationCardId);
        if (card!=null){
            callback.onCardLoaded(card);
        }else {
            callback.onDataNotAvailable();
        }
    }

    @Override
    public void saveRegistrationCard(@NonNull RegistrationCard registrationCard, @NonNull GetCardCallback callback) {
        RegistrationCardDao cardDao = mDaoSession.getRegistrationCardDao();
        cardDao.update(registrationCard);
        RegistrationCard card = cardDao.load(registrationCard.getRegistrationNumber());
        if (card!=null){
            callback.onCardLoaded(card);
        }else {
            callback.onDataNotAvailable();
        }
    }

    @Override
    public void deleteRegistrationCard(@NonNull String registrationCardId, @NonNull DeleteCardCallback callback) {
        RegistrationCardDao cardDao = mDaoSession.getRegistrationCardDao();
        //We need this queryBuilder() to delete all entities in bulk
        cardDao.queryBuilder()
                .where(RegistrationCardDao.Properties.RegistrationNumber.eq(registrationCardId))
                .buildDelete()
                .executeDeleteWithoutDetachingEntities();
        RegistrationCard card = cardDao.load(registrationCardId);
        if (card!=null){
            callback.onSuccess();
        }else {
            callback.onError();
        }
    }

    private void bootstrapDB() {
        List<Driver> drivers = new ArrayList<Driver>();
        for (int i = 0; i < 100 ; i++) {
            Driver driver = new Driver("driver"+i+"@driver.com",
                    "Test first name "+i, "Test last name "+i, "555-55-5"+i,"ADSF3456"+i);
            drivers.add(driver);
        }
    }
}
