package com.example.fruit.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Message;
import android.preference.PreferenceManager;

import com.example.fruit.MyAppliaction;

public class Util {
    private static final String PREF_NAME = "loginState";
    private static Util mInstance;
    private static SharedPreferences mSP;

    private static SharedPreferences.Editor mEditor;

    private Util() {
        Context context = MyAppliaction.getContext();
        mSP = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        mEditor = mSP.edit();
    }

    public static Util getInstance() {
        if (mInstance == null) {
            System.out.println("错误");
            mInstance = new Util();
        }
        return mInstance;
    }

    // 返回true表示登录，返回false表示未登录
    public static boolean getLoginState() {
        return mSP.getBoolean("isLogin", false);
    }

    public static void setLoginState(Boolean loginState) {
        mEditor.putBoolean("isLogin", loginState).commit();
    }

    public static String getUserName() {
        return mSP.getString("username", null);
    }

    public static void setUserName(String username) {
        mEditor.putString("username", username).commit();
    }

    public static void setCustomizeName(String customizeName) {
        mEditor.putString("name", customizeName).commit();
    }

    public static String getCustomizeName() {
        return mSP.getString("name", "");
    }

    public static void setProfile(String profile) {
        mEditor.putString("profile", profile).commit();
    }

    public static String getProfile() {
        return mSP.getString("profile", null);
    }

    public static void setNight(Boolean ifNight) {
        mEditor.putBoolean("night", ifNight).commit();
    }

    public static Boolean getNight() {
        return mSP.getBoolean("night", true);
    }

    public static void setNoHistory(Boolean ifNoHistory) {
        mEditor.putBoolean("noHistory", ifNoHistory).commit();
    }

    public static Boolean getNoHistory() {
        return mSP.getBoolean("noHistory", false);
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
