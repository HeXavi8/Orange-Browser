package com.example.fruit.search;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebBackForwardList;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fruit.MainActivity;
import com.example.fruit.MyAppliaction;
import com.example.fruit.R;
import com.example.fruit.bean.Collection;
import com.example.fruit.bean.History;
import com.example.fruit.home.HomeFragment;
import com.example.fruit.utils.Util;


import java.io.IOException;
import java.io.InputStream;
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
    private String mLoad;
    private static final String QUERY = "https://www.sogou.com/web?query=";
    private static final int SHOW_DIALOG = 0;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case SHOW_DIALOG:
                    showDialog(mLoad);
                    break;
            }
        }
    };

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

        mSearchRes.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction()==KeyEvent.ACTION_DOWN){
                    if(keyCode == KeyEvent.KEYCODE_BACK&&mSearchRes.canGoBack()){
                        mSearchRes.goBack();
                        return true;
                    }
                }
                return false;
            }
        });

        mSearchRes.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
         //手机浏览器模式调试
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mSearchRes.setWebContentsDebuggingEnabled(true);
        }
        //开启夜间模式的代码
//
//        String css="javascript: (function() {\n" +
//                "    css = document.createElement('link');" +
//                "    css.href = 'data:text/css,html,body,applet,object,h1,h2,h3,h4,h5,h6,blockquote,pre,abbr,acronym,address,big,cite,code,del,dfn,em,font,img,ins,kbd,q,p,s,samp,small,strike,strong,sub,sup,tt,var,b,u,i,center,dl,dt,dd,ol,ul,li,fieldset,form,label,legend,table,caption,tbody,tfoot,thead,th,td{background:rgba(0,0,0,0) !important;color:#fff !important;border-color:#A0A0A0 !important;}div,input,button,textarea,select,option,optgroup{background-color:#000 !important;color:#fff !important;border-color:#A0A0A0 !important;}a,a *{color:#ffffff !important; text-decoration:none !important;font-weight:bold !important;background-color:rgba(0,0,0,0) !important;}a:active,a:hover,a:active *,a:hover *{color:#1F72D0 !important;background-color:rgba(0,0,0,0) !important;}p,span{font color:#FF0000 !important;color:#ffffff !important;background-color:rgba(0,0,0,0) !important;}html{-webkit-filter: contrast(50%);}';\n" +
//                "    document.getElementsByTagName('html')[0].Backgroud(css);" +
//                "  \n" +
//                "})();";
//
//        mSearchRes.loadUrl(css);
//       System.out.print("hhhhh");
//       System.out.print(css);
//        mSearchRes.loadUrl("javascript:function()");

        mSearchRes.setWebViewClient(new MyWebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (BlockerTool.isBlock(MyAppliaction.getContext(), url)) {
                    mLoad = url;
                    Message message = new Message();
                    message.what = SHOW_DIALOG;
                    mHandler.sendMessage(message);
                } else {
                    view.loadUrl(url);
                }
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                mCollectionURL = url;

                setCurrentWebURL(mCollectionURL);
                setCurrentWebTitle(mCollectionTitle);

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

                //开启夜间模式
                try {
                    openNigth();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                mSearchRes.getSettings().setBlockNetworkImage(false);
                addImageClickListner();

                //注意这个坑，getTitle()要放在onPageFinished中，而不能放在onPageStarted中
                //否则获取到的title就是url
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
        load(mURL);
        return view;
    }

    private void showDialog(String url) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle(R.string.dialog_title);
        dialog.setMessage(R.string.dialog_message);
        dialog.setCancelable(false);
        dialog.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mSearchRes.loadUrl(url);
            }
        });
        dialog.setNegativeButton(R.string.CANCEL, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (mSearchRes.canGoBack()) {
                    mSearchRes.goBack();
                } else {
                    mActivity.replaceFragment(new HomeFragment());
                    mActivity.getTopSearch().setVisibility(View.GONE);
                }
            }
        });
        dialog.show();
    }

    public void load(String input) {
        if (BlockerTool.isBlock(getActivity(), input)) {
            showDialog(input);
        } else {
            if (isURL(input)) {
                mSearchRes.loadUrl(input);
                mActivity.getSearchUrl().setText(input);
            } else {
                mSearchRes.loadUrl(QUERY + input);
                mActivity.getSearchUrl().setText(QUERY + input);
            }
        }

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
        if (!Util.getInstance().getNoHistory()) {
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
        }
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
                "{"+
                "console.dir(objs[i]);"
                +"if(objs[i].onclick===null||objs[i].onclick==='')" +
                "   { objs[i].onclick=function()  " +
                "    {  "+
                " window.event ? window.event.cancelBubble = true : event.stopPropagation();"
                + "        window.imagelistner.openImage(this.src);  " +
                "    }  " +
                "}}" +
                "})()");
    }

    //新的方式开启夜间模式
    private void openNigth() throws IOException {
        InputStream is = mActivity.getResources().openRawResource(R.raw.night);
        System.out.print("打开夜间模式");
        byte[] buffer = new byte[0];
        try {
            buffer = new byte[is.available()];
            is.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String nightcode = Base64.encodeToString(buffer, Base64.NO_WRAP);
        mSearchRes.loadUrl("" +
                "javascript:(function() {" +
                "console.log('he');"+
                "var parent = document.getElementsByTagName('head').item(0);" +
                "var style = document.createElement('style');" + "style.type = 'text/css';" +
                "style.innerHTML = window.atob('" + nightcode + "');" + "parent.appendChild(style)" + "})();");
    }


}
