package com.example.fruit.search;

import com.example.fruit.MyAppliaction;
import com.example.fruit.bean.Collection;
import com.example.fruit.bean.History;
import com.example.fruit.dao.DBController;
import com.example.fruit.utils.Util;

import java.util.List;

public class SearchModel {
    private DBController mDBController;
    private String mUsername;

    public SearchModel() {
        mDBController = DBController.getInstance(MyAppliaction.getContext());
        mUsername = Util.getInstance().getUserName();
    }

    public void insertHistory(List<History> histories) {
        mDBController.insertHistory(histories);
    }

//    public void insertCollection(String url, String title) {
//        mDBController.addCollection(mUsername, url, title);
//    }
}
