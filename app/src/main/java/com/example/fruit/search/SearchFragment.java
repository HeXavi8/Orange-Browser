package com.example.fruit.search;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
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
import com.example.fruit.bean.Collection;
import com.example.fruit.bean.History;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SearchFragment extends Fragment {

    private static final String TAG = "TEST";
    private WebView mSearchRes;

    private String mURL;
    private String mCollectionURL;
    private String mCollectionTitle;

    private MainActivity mActivity;
    private LinearLayout mNavigationBar;
    private SearchPresenter mSearchPresenter;
    private FrameLayout mFullVideo;
    private ProgressBar progressBar;
    protected View mCustomView = null;
    private static final String QUERY = "https://www.sogou.com/web?query=";

    public void setURL(String url) {
        mURL = url;
    }

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

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

            super.onReceivedError(view, errorCode, description, failingUrl);

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
        mSearchRes.getSettings().setBlockNetworkImage(true);
        mSearchRes.addJavascriptInterface(new MJavascriptInterface(this.getActivity()),"imagelistner");
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
                mCollectionURL = url;

                //不能在此处调用getTitle()，应该在onPageFinished()
                //mCollectionTitle = mSearchRes.getTitle();

                setCurrentWebURL(mCollectionURL);
                setCurrentWebTitle(mCollectionTitle);

                System.out.println("check onPageStarted"+"URL"+mCollectionURL+"Title"+mCollectionTitle);//检测

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

            @Override
            public void onPageFinished(WebView view, String url) {
                mSearchRes.getSettings().setBlockNetworkImage(false);
                addImageClickListner();

                //注意这个坑，getTitle()需要在onPageFinished调用而不能在onPageStarted中使用
                //否则getTitle()返回的还是URL
                mCollectionTitle = mSearchRes.getTitle();

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

        //加入到presenter
        mSearchPresenter.insertHistories(historyList);

    };


    //set当前web页面的URL给到收藏collection
    public void setCurrentWebURL(String mCollectionURL){
        System.out.println("set URL:"+mCollectionURL);
        this.mCollectionURL = mCollectionURL;
    };

    //set当前web页面的title并给到收藏collection
    public void setCurrentWebTitle(String mCollectionTitle){
        System.out.println("set Title:"+mCollectionTitle);
        this.mCollectionTitle = mCollectionTitle;
    };

    //get当前web页面的URL给到收藏collection
    public String getCurrentWebURL(){
        System.out.println("get URL:"+mCollectionURL);
        return mCollectionURL;
    };

    //get当前web页面的title并给到收藏collection
    public String getCurrentWebTitle(){
        System.out.println("get Title:"+mCollectionTitle);
        return mCollectionTitle;
    };
/*...................................................................*/
    private void addImageClickListner() {
        // 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
        mSearchRes.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName(\"img\"); " +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "    objs[i].onclick=function()  " +
                "    {  "
                + "        window.imagelistner.openImage(this.src);  " +
                "    }  " +
                "}" +
                "})()");
        Log.d(TAG,"注入");
    }


}
