package com.example.fruit.home;

import com.example.fruit.MyAppliaction;
import com.example.fruit.bean.Quick;
import com.example.fruit.dao.DBController;

import java.util.List;

public class QuickModel {
    private DBController mDbController;

    public QuickModel() {
        mDbController = DBController.getInstance(MyAppliaction.getContext());
    }

    public boolean insertQuick(String title, String url) {
        return mDbController.insertQuick(title, url);
    }

    public List<Quick> getAllQuick() {
        return mDbController.getQuick();
    }

    public void deleteQuick(String title, String url) {
        mDbController.deleteQuick(title, url);
    }

    public void changeQuick(String title, String url, String newTitle, String newUrl) {
        mDbController.changeQuick(title, url, newTitle, newUrl);
    }
}
