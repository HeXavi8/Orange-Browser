package com.example.fruit.setting;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.fruit.R;
import com.example.fruit.utils.Util;


public class SettingsPresenter {
    private static final int NAME_CHANGED = 0;
    private static final int PROFILE_CHANGED = 1;
    private static final int CHECK_FALSE = 2;
    private static final int CHECK_SUCCESS = 3;
    private static final int DELETE_USER = 4;
    private static final int GET_PROFILE=5;

    private SettingsModel mSettingsModel;
    private SettingsView mSettingsView;
    private Boolean mCheckRes;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case NAME_CHANGED:
                    mSettingsView.showChangeName();
                    break;
                case PROFILE_CHANGED:
                    mSettingsView.showProfileAfterChange();
                    break;
                case CHECK_FALSE:
                    mSettingsView.showCheckPasswordFalse();
                    break;
                case CHECK_SUCCESS:
                    mSettingsView.showCheckPasswordSuccess();
                    break;
                case DELETE_USER:
                    mSettingsView.showDeleteUser();
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

    public void changeProfile(String profile) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mSettingsModel.setProfile(profile);
                Message message = new Message();
                message.what = PROFILE_CHANGED;
                mHandler.sendMessage(message);
            }
        }).start();

    }

    public void changePassword(String password, String newPassword) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mCheckRes = mSettingsModel.checkPassword(password);
                Message message = new Message();
                if (mCheckRes) {
                    mSettingsModel.changePassword(newPassword);
                    message.what = CHECK_SUCCESS;
                } else {
                    message.what = CHECK_FALSE;
                }
                mHandler.sendMessage(message);
            }
        }).start();
    }

    public void deleteUser() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mSettingsModel.deleteUser();
                Message message = new Message();
                message.what = DELETE_USER;
                mHandler.sendMessage(message);
            }
        }).start();
    }
    public void getProfile(){
        new Thread(new Runnable() {
            @Override
            public void run() {
               mSettingsModel.getProfile();
                Message message = new Message();
                message.what = GET_PROFILE;
                mHandler.sendMessage(message);

            }
        }).start();

    }
}
