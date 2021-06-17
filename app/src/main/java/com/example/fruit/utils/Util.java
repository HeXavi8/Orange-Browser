package com.example.fruit.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.fruit.MyAppliaction;

public class Util {
    private static final String PREF_NAME = "loginState";
    private static Util mInstance;
    private static SharedPreferences mSP;

    private Util() {
        Context context = MyAppliaction.getContext();
        SharedPreferences mSP = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        mSP.edit().putBoolean("isLogin", false).putString("username", "").apply();
    }

    public static Util getInstance() {
        if (mInstance == null) {
            mInstance = new Util();
        }
        return mInstance;
    }

    // 返回true表示登录，返回false表示未登录
    public static boolean getLoginState() {
        return !mSP.getBoolean("isLogin", false);
    }

    public static void setLoginState(Boolean loginState) {
        mSP.edit().putBoolean("isLogin", loginState).apply();
    }

    public static String getUserName() {
        return mSP.getString("username", "");
    }

    public static void setUserName(String username) {
        mSP.edit().putString("username", username).apply();
    }
}
