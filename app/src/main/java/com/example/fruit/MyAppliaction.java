package com.example.fruit;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import androidx.appcompat.app.AppCompatDelegate;

import com.example.fruit.utils.Util;

public class MyAppliaction extends Application {
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        //初始化我们的主题模式
//        if(Util.getNight()) {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//        };

    }

    public static Context getContext() {
        return sContext;
    }


}
