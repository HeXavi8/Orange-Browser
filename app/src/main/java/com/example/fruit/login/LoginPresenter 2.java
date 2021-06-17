package com.example.fruit.login;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

public class LoginPresenter {
    private static final int LOGIN_SUCCESSFULLY = 1;
    private static final int LOGIN_FAILED = 2;
    private LoginModel mLoginModel;
    private LoginView mLoginView;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case LOGIN_SUCCESSFULLY:
                    mLoginView.showLoginSuccessfully();
                    break;
                case LOGIN_FAILED:
                    mLoginView.showLoginFailed();
                    break;
            }
        }
    };

    public LoginPresenter(LoginView loginView) {
        mLoginModel = new LoginModel();
        mLoginView = loginView;
    }

    public void login(String username, String password) {
        mLoginModel.setUser(username, password);
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean res = mLoginModel.checkUserAndPassword();
                Message message = new Message();
                if (res) {
                    message.what = LOGIN_SUCCESSFULLY;
                } else {
                    message.what = LOGIN_FAILED;
                }
                mHandler.sendMessage(message);
            }
        }).start();
    }
}
