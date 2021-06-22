package com.example.fruit.setting;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;


public class SettingsPresenter {
    private static final int NAME_CHANGED = 0;

    private SettingsModel mSettingsModel;
    private SettingsView mSettingsView;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case NAME_CHANGED:
                    mSettingsView.showChangeName();
                    break;
            }
        }
    };

    public SettingsPresenter(SettingsView settingsView) {
        mSettingsModel = new SettingsModel();
        mSettingsView = settingsView;
    }

    public void changeName(String newName) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mSettingsModel.changeName(newName);
                Message message = new Message();
                message.what = NAME_CHANGED;
                mHandler.sendMessage(message);
            }
        }).start();
    }
}
