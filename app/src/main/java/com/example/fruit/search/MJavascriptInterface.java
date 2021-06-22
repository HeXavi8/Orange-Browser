package com.example.fruit.search;

import android.content.Context;
import android.content.Intent;

public class MJavascriptInterface {
    private Context context;

    public MJavascriptInterface(Context context) {
        this.context = context;
    }

    @android.webkit.JavascriptInterface
    public void openImage(String img) {
        Intent intent = new Intent();
        intent.putExtra("image", img);
        intent.setClass(context, PhotoBrowserActivity.class);
        context.startActivity(intent);
    }
}
