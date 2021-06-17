package com.example.fruit.search;

import android.app.Fragment;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebBackForwardList;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;

import com.example.fruit.MainActivity;
import com.example.fruit.R;
import com.example.fruit.bean.History;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SearchFragment extends Fragment {
    private WebView mSearchRes;
    private String mURL;
    private MainActivity mActivity;
    private LinearLayout mNavigationBar;
    private SearchPresenter mSearchPresenter;
    private FrameLayout mFullVideo;
    private ProgressBar progressBar;
    protected View mCustomView = null;
    private static final String QUERY = "https://www.sogou.com/web?query=";

    private class MyWebChromeClient extends WebChromeClient{

        @Override
        public void onHideCustomView() {
            if (mCustomView == null){
                return;
            }
            mFullVideo.removeView(mCustomView);
            mActivity.getTopSearch().setVisibility(View.VISIBLE);
            mActivity.getNavigationBar().setVisibility(View.VISIBLE);
            mFullVideo.setVisibility(View.GONE);
            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//清除全屏
        }

        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            mCustomView = view;
            mFullVideo.setVisibility(View.VISIBLE);
            mFullVideo.addView(mCustomView);
            mFullVideo.bringToFront();
            mActivity.getTopSearch().setVisibility(View.GONE);
            mActivity.getNavigationBar().setVisibility(View.GONE);
            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            mActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        }
    }


    private class MyWebViewClient extends WebViewClient{

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);
        mSearchPresenter = new SearchPresenter();
        mActivity = (MainActivity)getActivity();
        mSearchRes = (WebView)view.findViewById(R.id.web_view);
        mFullVideo = (FrameLayout)view.findViewById(R.id.full_video);
        progressBar = (ProgressBar)view.findViewById(R.id.progress);
        mNavigationBar = (LinearLayout)view.findViewById(R.id.navigation_bar);
        mSearchRes.getSettings().setJavaScriptEnabled(true);
        mSearchRes.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mSearchRes.getSettings().setSupportMultipleWindows(true);
        mSearchRes.getSettings().setBuiltInZoomControls(true);
        mSearchRes.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        mSearchRes.setWebViewClient(new MyWebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                mActivity.getSearchUrl().setText(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                mActivity.getSearchUrl().setText(url);
                if (view.canGoBack()) {
                    mActivity.getImageBack().setImageDrawable(getResources()
                            .getDrawable(R.drawable.back_page_enabled));
                    mActivity.backClick(SearchFragment.this);
                } else {
                    mActivity.getImageBack().setImageDrawable(getResources()
                            .getDrawable(R.drawable.back_page));
                }
                if (view.canGoForward()) {
                    mActivity.getImageForward().setImageDrawable(getResources()
                            .getDrawable(R.drawable.go_page_enabled));
                    mActivity.forwardClick(SearchFragment.this);
                } else {
                    mActivity.getImageForward().setImageDrawable(getResources()
                            .getDrawable(R.drawable.go_page));
                }
            }
        });
        mSearchRes.setWebChromeClient(new MyWebChromeClient());
        mSearchRes.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });
        mActivity.getTopSearch().setVisibility(View.VISIBLE);
        mURL = mActivity.getURL();
        System.out.println(mURL);
        if (isURL(mURL)) {
            mSearchRes.loadUrl(mURL);
            mActivity.getSearchUrl().setText(mURL);
        } else {
            mSearchRes.loadUrl(QUERY + mURL);
            mActivity.getSearchUrl().setText(QUERY + mURL);
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

    public void back() {
        mSearchRes.goBack();
    }

    public void forward() {
        mSearchRes.goForward();
    }

    @Override
    public void onPause() {
        super.onPause();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date());
        WebBackForwardList res = mSearchRes.copyBackForwardList();
        List<History> historyList = new ArrayList<History>();
        History history = new History();
        for (int i = 0; i < res.getSize(); i++) {
            history.setTitle(res.getItemAtIndex(i).getTitle());
            history.setUrl(res.getItemAtIndex(i).getUrl());
            history.setTime(date);
            historyList.add(history);
        }
        mSearchPresenter.insertHistories(historyList);
    }
}
