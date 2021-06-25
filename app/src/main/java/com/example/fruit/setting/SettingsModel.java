package com.example.fruit.setting;

import com.example.fruit.MyAppliaction;
import com.example.fruit.dao.DBController;
import com.example.fruit.utils.Util;

public class SettingsModel {
    private DBController mDBController;
    private String mUsername;

    public SettingsModel() {
        mDBController = new DBController(MyAppliaction.getContext());
        mUsername = Util.getInstance().getUserName();
    }

    public void changeName(String newName) {
        mDBController.changeUsername(mUsername, newName);
    }

    public void setProfile(String profile) {
        mDBController.setProfile(mUsername, profile);
    }
}
