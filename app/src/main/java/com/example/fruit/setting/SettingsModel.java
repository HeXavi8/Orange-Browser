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
    public String getProfile(){
        return mDBController.getProfile(mUsername);
    }

    public boolean checkPassword(String password) {
        return mDBController.checkUserAndPassword(mUsername, password) != null;
    }

    public void changePassword(String newPassword) {
        mDBController.changeUsername(mUsername, newPassword);
    }

    public void deleteUser() {
        mDBController.deleteUser(mUsername);
    }
}
