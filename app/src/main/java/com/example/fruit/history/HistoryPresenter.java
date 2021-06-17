package com.example.fruit.history;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import com.example.fruit.bean.History;

import java.util.List;

public class HistoryPresenter {
    private static final int SHOW_ALL_LIST = 0;
    private HistoryModel mHistoryModel;
    private HistoryView mHistoryView;
    private List<History> mHistories;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case SHOW_ALL_LIST:
                    mHistoryView.showAll(mHistories);
                    break;
            }
        }
    };
    public HistoryPresenter(HistoryView historyView) {
        mHistoryModel = new HistoryModel();
        mHistoryView = historyView;
    }

    //得到所有的历史记录并进行显示
    public void getHistories() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mHistories = mHistoryModel.getAllHistory();
                Message message = new Message();
                message.what = SHOW_ALL_LIST;
                mHandler.sendMessage(message);
            }
        }).start();
    }

    public void deleteAllHistory() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mHistoryModel.deleteAll();
            }
        }).start();
    }

    public void deleteSelectedHistory(List<History> toBeDeleted) {
        new Thread((new Runnable() {
            @Override
            public void run() {
                mHistoryModel.deleteSelected(toBeDeleted);
            }
        })).start();
    }
}
