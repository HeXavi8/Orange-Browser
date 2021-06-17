package com.example.fruit.history;

import com.example.fruit.bean.History;

import java.util.List;

public interface HistoryView {
    void showAll(List<History> histories);

    void showDeleteSelected();
}
