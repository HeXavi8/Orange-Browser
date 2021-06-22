package com.example.fruit.search;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import com.example.fruit.bean.History;

import java.util.List;

public class SearchPresenter {
    private SearchModel mSearchModel;

    public SearchPresenter() {
        mSearchModel = new SearchModel();
    }

    public void insertHistories(List<History> histories) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mSearchModel.insertHistory(histories);
            }
        }).start();
    }

//    public void insertCollection(String url, String title) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                mSearchModel.insertCollection(url,title);
//            }
//        }).start();
//    }


}
