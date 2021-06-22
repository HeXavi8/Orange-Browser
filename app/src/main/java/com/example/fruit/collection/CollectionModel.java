package com.example.fruit.collection;

import com.example.fruit.MyAppliaction;
import com.example.fruit.bean.Collection;
import com.example.fruit.dao.DBController;
import com.example.fruit.utils.Util;

import java.util.List;

public class CollectionModel {
    private DBController mDBController;
    private String mUsername;

    public CollectionModel() {
        mDBController = DBController.getInstance(MyAppliaction.getContext());
        mUsername = Util.getInstance().getUserName();
    }

    public List<Collection> getCollection() {
        return mDBController.getUserCollection(mUsername);
    }

    public void deleteAllUserCollection() {
        mDBController.deleteAllCollection(mUsername);
    }

    public void deleteSelectedUserCollection(List<Collection> toBeDeleted) {
        mDBController.deleteSelectedCollection(toBeDeleted);
    }
}
