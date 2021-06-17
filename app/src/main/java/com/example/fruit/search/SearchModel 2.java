package com.example.fruit.search;

import com.example.fruit.MyAppliaction;
import com.example.fruit.bean.History;
import com.example.fruit.dao.DBController;

import java.util.List;

public class SearchModel {
    private DBController mDBController;

    public SearchModel() {
        mDBController = DBController.getInstance(MyAppliaction.getContext());
    }

    public void insertHistory(List<History> histories) {
        mDBController.insertHistory(histories);
    }
}
