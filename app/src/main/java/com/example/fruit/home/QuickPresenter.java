package com.example.fruit.home;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import com.example.fruit.bean.Quick;

import java.util.List;


public class QuickPresenter {
    private static final int INSERT_SUCCESS = 0;
    private static final int INSERT_FAIL = 1;
    private static final int SHOW_ALL = 2;

    private QuickModel mQuickModel;
    private QuickView mQuickView;
    private List<Quick> mQuickList;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case INSERT_SUCCESS:
                    mQuickView.insertSuccess();
                    break;
                case INSERT_FAIL:
                    mQuickView.insertFail();;
                    break;
                case SHOW_ALL:
                    mQuickView.showAll(mQuickList);
                    break;
            }
        }
    };

    public QuickPresenter(QuickView quickView) {
        mQuickModel = new QuickModel();
        mQuickView = quickView;
    }

    public void insertQuick(String title, String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                if (mQuickModel.insertQuick(title, url)) {
                    message.what = INSERT_SUCCESS;
                }else {
                    message.what = INSERT_FAIL;
                }
                mHandler.sendMessage(message);
            }
        }).start();
    }

    public void getAllQuick() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mQuickList = mQuickModel.getAllQuick();
                Message message = new Message();
                message.what = SHOW_ALL;
                mHandler.sendMessage(message);
            }
        }).start();
    }

    public void deleteQuick(String title, String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mQuickModel.deleteQuick(title, url);
            }
        }).start();
    }

    public void changeQuick(String title, String url, String newTitle, String newUrl) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mQuickModel.changeQuick(title, url, newTitle, newUrl);
            }
        }).start();
    }
}
