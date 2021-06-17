package com.example.fruit.history;

import com.example.fruit.MyAppliaction;
import com.example.fruit.bean.History;
import com.example.fruit.dao.DBController;

import java.util.List;

public class HistoryModel {
    private DBController mDBController;

    public HistoryModel() {
        mDBController = new DBController(MyAppliaction.getContext());
    }

    public List<History> getAllHistory() {
        return mDBController.getAll();
    }

    public void deleteAll() {
        mDBController.deleteAllHistory();
    }

    public void deleteSelected(List<History> toBeDeleted) {
        mDBController.deleteSelectedHistory(toBeDeleted);
    }
}
