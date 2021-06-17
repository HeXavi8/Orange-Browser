package com.example.fruit.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.fruit.bean.Collection;
import com.example.fruit.bean.CollectionDao;
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
    private CollectionDao mCollectionDao;
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
        mCollectionDao = mDaoSession.getCollectionDao();
    }

    private SQLiteDatabase getWrittableDatabase() {
        if (mHelpler == null) {
            mHelpler = new DaoMaster.DevOpenHelper(mContext, DB_NAME, null);
        }
        mDb = mHelpler.getWritableDatabase();
        return mDb;
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
            List<History>res = mHistoryDao.queryBuilder()
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

    public List<Collection> getUserCollection(String username) {
        List<Collection> res = mCollectionDao.queryBuilder()
                .where(CollectionDao.Properties.Name.eq(username)).build().list();
        return res;
    }

    public long addCollection(String username, String url, String title) {
        List<Collection> exist = mCollectionDao.queryBuilder()
                .where(CollectionDao.Properties.Name.eq(username),
                CollectionDao.Properties.Url.eq(url), CollectionDao.Properties.Title.eq(title))
                .build().list();
        if (exist == null || exist.isEmpty()) {
            return mCollectionDao.insert(new Collection(username, url, title));
        } else {
            return -1;
        }
    }

    public void deleteAllCollection(String username) {
        mCollectionDao.queryBuilder().where(CollectionDao.Properties.Name.eq(username))
                .buildDelete().executeDeleteWithoutDetachingEntities();
    }

    public void deleteSelectedCollection(List<Collection> toBeDeleted) {
        if (mDb.isOpen()) {
            try {
                mDb.beginTransaction();
                for (int i = 0; i < toBeDeleted.size(); i++) {
                    mCollectionDao.queryBuilder()
                            .where(CollectionDao.Properties.Name.eq(toBeDeleted.get(i).getName()),
                            CollectionDao.Properties.Url.eq(toBeDeleted.get(i).getUrl()),
                            CollectionDao.Properties.Title.eq(toBeDeleted.get(i).getTitle()));
                }
                mDb.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mDb.endTransaction();
            }
        }
    }
}
