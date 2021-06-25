package com.example.fruit.login;

import com.example.fruit.bean.User;

public interface LoginView {
    void showLoginSuccessfully(User user);

    void showLoginFailed();
}
