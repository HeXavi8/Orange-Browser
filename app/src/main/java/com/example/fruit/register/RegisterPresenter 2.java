package com.example.fruit.register;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

public class RegisterPresenter {
    private static final int REGISTER_SUCCESSFULLY = 1;
    private static final int USERNAME_EXIST = 2;
    private RegisterModel mRegisterModel;
    private RegisterView mRegisterView;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case REGISTER_SUCCESSFULLY:
                    mRegisterView.showRegisterSuccessfully();
                    break;
                case USERNAME_EXIST:
                    mRegisterView.showUsernameExist();
                    break;
            }
        }
    };

    public RegisterPresenter(RegisterView registerView) {
        mRegisterModel = new RegisterModel();
        mRegisterView = registerView;
    }

    public void register(String username, String password) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean res = mRegisterModel.checkUserExist(username);
                Message message = new Message();
                if (res) {
                    message.what = USERNAME_EXIST;
                } else {
                    mRegisterModel.insertUser(username, password);
                    message.what = REGISTER_SUCCESSFULLY;
                }
                mHandler.sendMessage(message);
            }
        }).start();
    }
}
