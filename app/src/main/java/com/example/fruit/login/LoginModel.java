package com.example.fruit.login;

import com.example.fruit.MyAppliaction;
import com.example.fruit.bean.User;
import com.example.fruit.dao.DBController;

public class LoginModel {
    private DBController mDbController;
    private User mUser;

    public LoginModel() {
        mUser = new User(null, "", "", "");
        mDbController = DBController.getInstance(MyAppliaction.getContext());
    }

    public User checkUserAndPassword () {
        return mDbController.checkUserAndPassword(mUser.getName(), mUser.getPassword());
    }

    public void setUser(String username, String password) {
        mUser.setName(username);
        mUser.setPassword(password);
        mUser.setCustomizeName(username);
    }
}
