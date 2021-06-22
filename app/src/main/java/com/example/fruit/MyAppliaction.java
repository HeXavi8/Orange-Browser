package com.example.fruit;

import android.app.Application;
import android.content.Context;

import com.example.fruit.utils.Util;

public class MyAppliaction extends Application {
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();

    }

    public static Context getContext() {
        return sContext;
    }
}
