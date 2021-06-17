package com.example.fruit.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.fruit.bean.DaoMaster;
import com.example.fruit.bean.DaoSession;
import com.example.fruit.bean.History;
import com.example.fruit.bean.HistoryDao;
import com.example.fruit.bean.User;
import com.example.fruit.bean.UserDao;

import java.util.ArrayList;
import java.util.List;

public class DBController {
    private static final String DB_NAME = "fruit.db";
    private DaoMaster.DevOpenHelper mHelpler;
    private SQLiteDatabase mDb;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private Context mContext;
    private UserDao mUserDao;
    private HistoryDao mHistoryDao;
    private static DBController sDbController;

    public static DBController getInstance(Context context) {
        if (sDbController == null) {
            synchronized (DBController.class){
                if (sDbController == null) {
                    sDbController = new DBController(context);
                }
            }
        }
        return sDbController;
    }

    public DBController(Context context) {
        mContext = context;
        mHelpler = new DaoMaster.DevOpenHelper(mContext, DB_NAME, null);
        mDaoMaster = new DaoMaster(getWrittableDatabase());
        mDaoSession = mDaoMaster.newSession();
        mUserDao = mDaoSession.getUserDao();
        mHistoryDao = mDaoSession.getHistoryDao();
    }

    private SQLiteDatabase getWrittableDatabase() {
        if (mHelpler == null) {
            mHelpler = new DaoMaster.DevOpenHelper(mContext, DB_NAME, null);
        }
        SQLiteDatabase db = mHelpler.getWritableDatabase();
        return db;
    }

    public long insertUser(User user) {
        return mUserDao.insert(user);
    }

    public boolean checkUserAndPassword(String whereUser, String wherePassword) {
        List<User>users = (List<User>)mUserDao.queryBuilder()
                .where(UserDao.Properties.Name.eq(whereUser),
                        UserDao.Properties.Password.eq(wherePassword)).build().list();
        return !users.isEmpty();
    }

    public boolean checkUserExist(String whereUser) {
        List<User>users = (List<User>)mUserDao.queryBuilder()
                .where(UserDao.Properties.Name.eq(whereUser)).build().list();
        return !users.isEmpty();
    }

    public void insertHistory(List<History> histories) {
        for (int i = 0; i < histories.size(); i++) {
            List<History>res = (List<History>)mHistoryDao.queryBuilder()
                    .where(HistoryDao.Properties.Url.eq(histories.get(i).getUrl()),
                            HistoryDao.Properties.Time.eq(histories.get(i).getTime()))
                    .build().list();
            if (res == null || res.isEmpty()) {
                mHistoryDao.insert(histories.get(i));
            } else {
                mHistoryDao.queryBuilder().where(HistoryDao.Properties.Url.eq(histories.get(i).getUrl()),
                        HistoryDao.Properties.Time.eq(histories.get(i).getTime())).buildDelete()
                        .executeDeleteWithoutDetachingEntities();
                mHistoryDao.insert(histories.get(i));
            }
        }
    }

    public void deleteAllHistory() {
        mHistoryDao.deleteAll();
    }

    public void deleteSelectedHistory(List<History> historiesToBeDeleted) {
        if (mDb.isOpen()) {
            try {
                mDb.beginTransaction();
                for (int i = 0; i < historiesToBeDeleted.size(); i++) {
                    mHistoryDao.queryBuilder()
                            .where(HistoryDao.Properties.Url.eq(historiesToBeDeleted.get(i).getUrl()),
                            HistoryDao.Properties.Time.eq(historiesToBeDeleted.get(i).getTime()))
                            .buildDelete()
                            .executeDeleteWithoutDetachingEntities();
                }
                mDb.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mDb.endTransaction();
            }
        }
    }

    public List<History> getAll() {
        List<History> all = (List<History>)mHistoryDao.queryBuilder().build().list();
        return all;
    }
}
