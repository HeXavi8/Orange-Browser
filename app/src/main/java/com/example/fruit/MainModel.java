package com.example.fruit;

import com.example.fruit.dao.DBController;
import com.example.fruit.utils.Util;

public class MainModel {
    private DBController mDBController;
    private String username;

    public MainModel() {
        mDBController = DBController.getInstance(MyAppliaction.getContext());
        username = Util.getInstance().getUserName();
    }

    public void addCollection(String url, String title) {
        mDBController.addCollection(username, url, title);
    }
}
