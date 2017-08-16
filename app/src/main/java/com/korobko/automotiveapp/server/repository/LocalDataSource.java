/*
 * Copyright (c) 2017.  | Volodymyr Korobko | volodymyr.korobko@gmail.com
 */

package com.korobko.automotiveapp.server.repository;

import android.content.Context;
import android.support.annotation.NonNull;

import com.korobko.automotiveapp.DataSource;
import com.korobko.automotiveapp.models.Car;
import com.korobko.automotiveapp.models.Driver;
import com.korobko.automotiveapp.models.EntityRegCard;
import com.korobko.automotiveapp.models.RegistrationCard;
import com.korobko.automotiveapp.server.orm.CarDao;
import com.korobko.automotiveapp.server.orm.DaoMaster;
import com.korobko.automotiveapp.server.orm.DaoSession;
import com.korobko.automotiveapp.server.orm.DriverDao;
import com.korobko.automotiveapp.server.orm.EntityRegCardDao;

import org.greenrobot.greendao.database.Database;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
        EntityRegCardDao cardDao = mDaoSession.getEntityRegCardDao();
        CarDao carDao = mDaoSession.getCarDao();
        EntityRegCard entityRegCard = RegistrationCardMapper.toInternal(registrationCard);

        //We need insert each car entity manually
        cardDao.insert(entityRegCard);

        entityRegCard.getCars().forEach(carDao::insert);

        EntityRegCard card = cardDao.load(registrationCard.getRegistrationNumber());
        if (card!=null){
            callback.onSuccess();
        }else {
            callback.onError();
        }

    }

    @Override
    public void getRegistrationCards(final @NonNull LoadCardsCallback callback) {
        EntityRegCardDao cardDao = mDaoSession.getEntityRegCardDao();
        List<EntityRegCard> cards = cardDao.loadAll();
        if (cards.isEmpty()){
            callback.onDataNotAvailable();
        }else {
            List<RegistrationCard> registrationCards = new ArrayList<>();
            cards.forEach(card -> {
                RegistrationCard registrationCard = RegistrationCardMapper.fromInternal(card);
                registrationCards.add(registrationCard);
            });

            callback.onCardsLoaded(registrationCards);
        }
    }


    @Override
    public void getRegistrationCard(@NonNull String registrationCardId, final @NonNull GetCardCallback callback) {
        EntityRegCardDao cardDao = mDaoSession.getEntityRegCardDao();
        EntityRegCard entityRegCard = cardDao.load(registrationCardId);
        if (entityRegCard!=null){
        RegistrationCard card = RegistrationCardMapper.fromInternal(entityRegCard);

//            //Relations are resolved on first access so we have to call this
//            //methods to initialize entities
//            card.setDriver(driverDao.load(card.getId_driver()));
//            card.getCars();
//            //Then just return entity
            callback.onCardLoaded(card);
        }else {
            callback.onDataNotAvailable();
        }
    }

    @Override
    public void saveRegistrationCard(@NonNull RegistrationCard registrationCard, @NonNull GetCardCallback callback) {
        EntityRegCardDao cardDao = mDaoSession.getEntityRegCardDao();
        CarDao carDao = mDaoSession.getCarDao();
        EntityRegCard entityRegCard = RegistrationCardMapper.toInternal(registrationCard);
        //We need update each car entity manually
        cardDao.update(entityRegCard);

        entityRegCard.getCars().forEach(carDao::update);

        EntityRegCard regCard = cardDao.load(registrationCard.getRegistrationNumber());
        if (regCard!=null){
            RegistrationCard card = RegistrationCardMapper.fromInternal(regCard);
            callback.onCardLoaded(card);
        }else {
            callback.onDataNotAvailable();
        }
    }

    @Override
    public void deleteRegistrationCard(@NonNull String registrationCardId, @NonNull DeleteCardCallback callback) {
        EntityRegCardDao cardDao = mDaoSession.getEntityRegCardDao();
        //We need this queryBuilder() to delete all entities in bulk
        cardDao.queryBuilder()
                .where(EntityRegCardDao.Properties.RegistrationNumber.eq(registrationCardId))
                .buildDelete()
                .executeDeleteWithoutDetachingEntities();
        EntityRegCard card = cardDao.load(registrationCardId);
        if (card!=null){
            callback.onSuccess();
        }else {
            callback.onError();
        }
    }

    private void bootstrapDB() {
        EntityRegCardDao cardDao = mDaoSession.getEntityRegCardDao();
        CarDao carDao = mDaoSession.getCarDao();
        for (int i = 0; i < 100 ; i++) {
            EntityRegCard card = new EntityRegCard();
            card.setRegistrationNumber("driver"+i+"@driver.com");
            Driver driver = new Driver(UUID.randomUUID().toString(),
                    "Test first name "+i, "Test last name "+i, "555-55-5"+i,"ADSF3456"+i);
            card.setDriver(driver);
            cardDao.insert(card);
            for (int j = 0; j < 5; j++) {
                Car car = new Car(UUID.randomUUID().toString(),
                        "Test make "+i, "Test type "+i, "test color"+i);
                car.setId_reg_card(card.getRegistrationNumber());
                carDao.insert(car);
            }
        }

    }
}
