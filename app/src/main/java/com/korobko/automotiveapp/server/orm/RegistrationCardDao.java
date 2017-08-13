/*
 * Copyright (c) 2017.  | Volodymyr Korobko | volodymyr.korobko@gmail.com
 */

package com.korobko.automotiveapp.server.orm;

import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.SqlUtils;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.korobko.automotiveapp.restapi.Driver;

import com.korobko.automotiveapp.restapi.RegistrationCard;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "REGISTRATION_CARD".
*/
public class RegistrationCardDao extends AbstractDao<RegistrationCard, String> {

    public static final String TABLENAME = "REGISTRATION_CARD";

    /**
     * Properties of entity RegistrationCard.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property RegistrationNumber = new Property(0, String.class, "registrationNumber", true, "REGISTRATION_NUMBER");
        public final static Property Id_driver = new Property(1, String.class, "id_driver", false, "ID_DRIVER");
    }

    private DaoSession daoSession;


    public RegistrationCardDao(DaoConfig config) {
        super(config);
    }
    
    public RegistrationCardDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"REGISTRATION_CARD\" (" + //
                "\"REGISTRATION_NUMBER\" TEXT PRIMARY KEY NOT NULL ," + // 0: registrationNumber
                "\"ID_DRIVER\" TEXT);"); // 1: id_driver
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"REGISTRATION_CARD\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, RegistrationCard entity) {
        stmt.clearBindings();
        stmt.bindString(1, entity.getRegistrationNumber());
 
        String id_driver = entity.getId_driver();
        if (id_driver != null) {
            stmt.bindString(2, id_driver);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, RegistrationCard entity) {
        stmt.clearBindings();
        stmt.bindString(1, entity.getRegistrationNumber());
 
        String id_driver = entity.getId_driver();
        if (id_driver != null) {
            stmt.bindString(2, id_driver);
        }
    }

    @Override
    protected final void attachEntity(RegistrationCard entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.getString(offset + 0);
    }    

    @Override
    public RegistrationCard readEntity(Cursor cursor, int offset) {
        RegistrationCard entity = new RegistrationCard( //
            cursor.getString(offset + 0), // registrationNumber
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1) // id_driver
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, RegistrationCard entity, int offset) {
        entity.setRegistrationNumber(cursor.getString(offset + 0));
        entity.setId_driver(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
     }
    
    @Override
    protected final String updateKeyAfterInsert(RegistrationCard entity, long rowId) {
        return entity.getRegistrationNumber();
    }
    
    @Override
    public String getKey(RegistrationCard entity) {
        if(entity != null) {
            return entity.getRegistrationNumber();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(RegistrationCard entity) {
        throw new UnsupportedOperationException("Unsupported for entities with a non-null key");
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getDriverDao().getAllColumns());
            builder.append(" FROM REGISTRATION_CARD T");
            builder.append(" LEFT JOIN DRIVER T0 ON T.\"ID_DRIVER\"=T0.\"ID\"");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected RegistrationCard loadCurrentDeep(Cursor cursor, boolean lock) {
        RegistrationCard entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        Driver driver = loadCurrentOther(daoSession.getDriverDao(), cursor, offset);
        entity.setDriver(driver);

        return entity;    
    }

    public RegistrationCard loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();
        
        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);
        
        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }
    
    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<RegistrationCard> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<RegistrationCard> list = new ArrayList<RegistrationCard>(count);
        
        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }
    
    protected List<RegistrationCard> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<RegistrationCard> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
