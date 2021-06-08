package com.example.fruit.search;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;

import com.example.fruit.MainActivity;
import com.example.fruit.R;

public class SearchFragment extends Fragment {
    private WebView mSearchRes;
    private String mURL;
    private MainActivity mActivity;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);
        mSearchRes = (WebView)view.findViewById(R.id.web_view);
        mSearchRes.getSettings().setJavaScriptEnabled(true);
        mSearchRes.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mSearchRes.getSettings().setSupportMultipleWindows(true);
        mSearchRes.getSettings().setBuiltInZoomControls(true);
        mSearchRes.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        mSearchRes.setWebViewClient(new WebViewClient());
        mSearchRes.setWebChromeClient(new WebChromeClient());
        mSearchRes.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });
        mActivity = (MainActivity)getActivity();
        mActivity.getTopSearch().setVisibility(View.VISIBLE);
        mURL = mActivity.getURL();
        System.out.println(mURL);
        if (isURL(mURL)) {
            mSearchRes.loadUrl(mURL);
        } else {
            mSearchRes.loadUrl("https://www.baidu.com/s?wd="+mURL);
        }
        return view;
    }

    private boolean isURL(String str){
        str = str.toLowerCase();
        String regex = "^((https|http|ftp|rtsp|mms)?://)"
                + "?(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?"
                + "(([0-9]{1,3}\\.){3}[0-9]{1,3}"
                + "|"
                + "([0-9a-z_!~*'()-]+\\.)*"
                + "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\\."
                + "[a-z]{2,6})"
                + "(:[0-9]{1,5})?"
                + "((/?)|"
                + "(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$";
        return  str.matches(regex);
    }
}
