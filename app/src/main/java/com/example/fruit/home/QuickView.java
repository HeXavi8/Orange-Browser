package com.example.fruit.home;

import com.example.fruit.bean.Quick;

import java.util.List;

public interface QuickView {
    void insertSuccess();

    void insertFail();

    void showAll(List<Quick> quicks);
}
