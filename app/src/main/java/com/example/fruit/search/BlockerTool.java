package com.example.fruit.search;

import android.content.Context;
import android.content.res.Resources;

import com.example.fruit.R;

public class BlockerTool {
    public static boolean isBlock(Context context, String url) {
        Resources resources = context.getResources();
        String [] urlsToBeBlocked = resources.getStringArray(R.array.BlockUrl);
        for (String adURL : urlsToBeBlocked) {
            if (matchURL(url, adURL)) {
                return true;
            }
        }
        return false;
    }

    private static boolean matchURL(String sourceURL, String targetURL) {
        return sourceURL.contains(targetURL);
    }
}
