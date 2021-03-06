package com.example.fruit.search;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebBackForwardList;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fruit.MainActivity;
import com.example.fruit.MyAppliaction;
import com.example.fruit.R;
import com.example.fruit.bean.History;
import com.example.fruit.home.HomeFragment;
import com.example.fruit.utils.Util;


import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SearchFragment extends Fragment{

    private static final String TAG = "TEST";
    private WebView mSearchRes;

    private String mURL;
    private String mCollectionURL;
    private String mCollectionTitle;

    private MainActivity mActivity;
    private LinearLayout mNavigationBar;
    private WindowManager.LayoutParams lpa;
    private MainActivity.MyTouchListener myTouchListener, WebViewTouchListener;
    private SearchPresenter mSearchPresenter;
    private FrameLayout mFullVideo;
    protected View mCustomView = null;
    private String mLoad;
    private static final String QUERY = "https://www.sogou.com/web?query=";
    //private static final String QUERY = "https://cn.bing.com/search?q=";
    private static final int SHOW_DIALOG = 0;

    private GestureDetector mGestureDetector;
    private int maxVolume,currentVolume;
    private int playerWidth, playerHeight;
    private AudioManager audiomanager;
    private float mBrightness = -1f; // ??????
    private float mBrightnessHelperY;
    private float mBrightnessHelperX;

    private float mTopSearchY;

    private static final float STEP_VOLUME = 2f;// ?????????????????????????????????????????????????????????????????????????????????
    private boolean firstScroll = false;// ?????????????????????????????????scroll?????????
    private int GESTURE_FLAG = 0;// 1,???????????????2???????????????,3.????????????
    private static final int GESTURE_MODIFY_PROGRESS = 1;
    private static final int GESTURE_MODIFY_VOLUME = 2;
    private static final int GESTURE_MODIFY_BRIGHT = 3;

    private RelativeLayout change_vol_bright;
    private ImageView mGestureImage;
    private TextView mGestureTextView;

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
            mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//????????????
            mActivity.unRegisterMyTouchListener(myTouchListener);
            lpa.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
            mActivity.getWindow().setAttributes(lpa);

        }

        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            lpa = mActivity.getWindow().getAttributes();
            mFullVideo.setFocusable(true);
            mFullVideo.setClickable(true);
            mFullVideo.setLongClickable(true);
            audiomanager = (AudioManager)getActivity().getSystemService(Context.AUDIO_SERVICE);
            maxVolume = audiomanager.getStreamMaxVolume(AudioManager.STREAM_MUSIC); // ????????????????????????
            currentVolume = audiomanager.getStreamVolume(AudioManager.STREAM_MUSIC); // ???????????????

            ViewTreeObserver viewObserver = mFullVideo.getViewTreeObserver();
            viewObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mFullVideo.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    playerWidth = mFullVideo.getWidth();
                    playerHeight = mFullVideo.getHeight();
                }
            });

            mActivity.registerMyTouchListener(myTouchListener);
            mCustomView = view;
            mFullVideo.setVisibility(View.VISIBLE);
            mFullVideo.addView(mCustomView);
            mFullVideo.bringToFront();
            mActivity.getTopSearch().setVisibility(View.GONE);
            mActivity.getNavigationBar().setVisibility(View.GONE);
            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            mActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//????????????
        }
    }


    private class MyWebViewClient extends WebViewClient{

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

            super.onReceivedError(view, errorCode, description, failingUrl);

        }
    }

    @Override
    public void onDestroyView() {
        mActivity.unRegisterMyTouchListener(WebViewTouchListener);
        super.onDestroyView();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);
        mSearchPresenter = new SearchPresenter();
        mActivity = (MainActivity)getActivity();
        ((MainActivity)getActivity()).getNavigationBar().setVisibility(View.VISIBLE);
        ((MainActivity)getActivity()).getTopSearch().setVisibility(View.VISIBLE);
        mSearchRes = (WebView)view.findViewById(R.id.web_view);
        mFullVideo = (FrameLayout)view.findViewById(R.id.full_video);
        mNavigationBar = (LinearLayout)view.findViewById(R.id.navigation_bar);
        mSearchRes.getSettings().setJavaScriptEnabled(true);
        mSearchRes.getSettings().setBlockNetworkImage(true);
        mSearchRes.addJavascriptInterface(new MJavascriptInterface(this.getActivity()),"imagelistner");
        mSearchRes.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mSearchRes.getSettings().setSupportMultipleWindows(true);
        mSearchRes.getSettings().setBuiltInZoomControls(true);
        mSearchRes.setFocusable(true);
        mSearchRes.setFocusableInTouchMode(true);

        mGestureDetector = new GestureDetector(new gestureListener());
        change_vol_bright = (RelativeLayout) view.findViewById(R.id.gesture_layout);
        mGestureImage = (ImageView)view.findViewById(R.id.volume_bright_image);
        mGestureTextView=(TextView)view.findViewById(R.id.percentage_volume_bright);




        myTouchListener = new MainActivity.MyTouchListener() {
            @Override
            public void onTouchEvent(MotionEvent event) {
                Log.d(TAG,"myTouchListener: "+Integer.toString(event.getAction()));
                boolean detectedUp = event.getAction() == MotionEvent.ACTION_UP;
                //?????????????????????????????????????????????????????????????????????
                if(!detectedUp){
                    mGestureDetector.onTouchEvent(event);
                }else {
                    change_vol_bright.setVisibility(View.GONE);
                }

            }
        };

        WebViewTouchListener = new MainActivity.MyTouchListener() {
            @Override
            public void onTouchEvent(MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    float mDownY = event.getRawY()-MainActivity.mTitleBarHeight;
                    mTopSearchY = mActivity.getTopSearch().getBottom();
                    if(mDownY>mTopSearchY){
                        mSearchRes.requestFocus();
                        InputMethodManager imm = (InputMethodManager)mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(mSearchRes.getWindowToken(), 0);
                        mActivity.setSearchUrl(getCurrentWebURL());
                    }
                }
            }
        };
        mActivity.registerMyTouchListener(WebViewTouchListener);

        mSearchRes.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction()==KeyEvent.ACTION_DOWN){
                    if(keyCode == KeyEvent.KEYCODE_BACK&&mSearchRes.canGoBack()){
                        mSearchRes.goBack();
                        return true;
                    }
                    else if(keyCode == KeyEvent.KEYCODE_BACK){
                        mActivity.replaceFragment(new HomeFragment());
                    }
                }
                return false;
            }
        });

        mSearchRes.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
         //???????????????????????????
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mSearchRes.setWebContentsDebuggingEnabled(true);
        }

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
                mActivity.getTopSearch().setFocusable(false);
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

                //????????????????????????????????????webview
                if(!Util.getInstance().getNight()) {
                    try {
                        openNigth();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                mSearchRes.getSettings().setBlockNetworkImage(false);
                addImageClickListner();

                mCollectionTitle = mSearchRes.getTitle();

            }

        });
        mSearchRes.setWebChromeClient(new MyWebChromeClient());
        mActivity.getTopSearch().setVisibility(View.VISIBLE);
        load(mURL);
        return view;
    }
    private void showDialog(String url) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_title);
        builder.setMessage(R.string.dialog_message);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mSearchRes.loadUrl(url);
            }
        });
        builder.setNegativeButton(R.string.CANCEL, new DialogInterface.OnClickListener() {
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
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.search_hint_text));
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.search_hint_text));

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

            mSearchPresenter.insertHistories(historyList);
        }
    };

    //set??????web?????????URL????????????collection
    public void setCurrentWebURL(String mCollectionURL){
        System.out.println("set URL:"+mCollectionURL);
        this.mCollectionURL = mCollectionURL;
    };

    //set??????web?????????title???????????????collection
    public void setCurrentWebTitle(String mCollectionTitle){
        System.out.println("set Title:"+mCollectionTitle);
        this.mCollectionTitle = mCollectionTitle;
    };

    //get??????web?????????URL????????????collection
    public String getCurrentWebURL(){
        System.out.println("get URL:"+mCollectionURL);
        return mCollectionURL;
    };

    //get??????web?????????title???????????????collection
    public String getCurrentWebTitle(){
        System.out.println("get Title:"+mCollectionTitle);
        return mCollectionTitle;
    };

    private void addImageClickListner() {
        // ??????js???????????????????????????????????????img??????????????????onclick???????????????????????????????????????????????????????????????java???????????????url??????
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

    //webview??????????????????
    private void openNigth() throws IOException {
        InputStream is = mActivity.getResources().openRawResource(R.raw.night);
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
                "var parent = document.getElementsByTagName('head').item(0);" +
                "var style = document.createElement('style');" + "style.type = 'text/css';" +
                "style.innerHTML = window.atob('" + nightcode + "');" + "parent.appendChild(style)" + "})();");
    }

    private class gestureListener implements GestureDetector.OnGestureListener{

        public boolean onDown(MotionEvent e) {
            Log.i("MyGesture", "onDown");
            firstScroll = true;
            return false;
        }

        public void onShowPress(MotionEvent e) {
            Log.i("MyGesture", "onShowPress");

        }

        public boolean onSingleTapUp(MotionEvent e) {
            Log.i("MyGesture", "onSingleTapUp");

            return true;
        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//            Log.i("MyGesture22", "onScroll:"+e1.getY() +"   "+distanceX);
            change_vol_bright.bringToFront();
            change_vol_bright.setVisibility(View.VISIBLE);
            float mOldX = e1.getX(), mOldY = e1.getY();
            int y = (int) e2.getRawY();
            if(e1.getX()==mBrightnessHelperX) {
                mOldY = mBrightnessHelperY;
            }
            mBrightnessHelperY = e2.getRawY();
            mBrightnessHelperX = e1.getX();
            if (firstScroll) {// ?????????????????????????????????????????????????????????????????????????????????
                // ???????????????????????????????????????????????????????????????????????????
                if (Math.abs(distanceX) >= Math.abs(distanceY)) {
                    GESTURE_FLAG = GESTURE_MODIFY_PROGRESS;
                } else {
                    if (mOldX > playerWidth * 3.0 / 5) {// ??????
                        mGestureImage.setImageResource(R.drawable.player_volume);
                        GESTURE_FLAG = GESTURE_MODIFY_VOLUME;
                    } else if (mOldX < playerWidth * 2.0 / 5) {// ??????
                        mGestureImage.setImageResource(R.drawable.player_bright);
                        GESTURE_FLAG = GESTURE_MODIFY_BRIGHT;
                    }
                }
            }
            Log.d("MyGesture",Integer.toString(GESTURE_FLAG));
            // ????????????????????????????????????scroll??????????????????????????????scroll?????????????????????????????????????????????????????????????????????
            if (GESTURE_FLAG == GESTURE_MODIFY_PROGRESS) {

            }
            // ????????????????????????????????????scroll??????????????????????????????scroll?????????????????????????????????????????????????????????????????????
            else if (GESTURE_FLAG == GESTURE_MODIFY_VOLUME) {
                currentVolume = audiomanager.getStreamVolume(AudioManager.STREAM_MUSIC); // ???????????????
                if (Math.abs(distanceY) > Math.abs(distanceX)) {// ??????????????????????????????
                    if (distanceY >= STEP_VOLUME) {// ????????????,??????????????????????????????,???????????????????????????????????????????????????distanceY??????
                        if (currentVolume < maxVolume) {// ????????????????????????distanceY????????????????????????
                            currentVolume++;
                        }
                    } else if (distanceY <= -STEP_VOLUME) {// ????????????
                        if (currentVolume > 0) {
                            currentVolume--;
                            if (currentVolume == 0) {// ????????????????????????????????????
                            }
                        }
                    }
                    double percentage_volume = currentVolume * 1.0 / maxVolume;
                    mGestureTextView.setText((int)(percentage_volume*100)+"%");
                    audiomanager.setStreamVolume(AudioManager.STREAM_MUSIC,currentVolume, 0);
                }
            }
            // ????????????????????????????????????scroll??????????????????????????????scroll?????????????????????????????????????????????????????????????????????
            else if (GESTURE_FLAG == GESTURE_MODIFY_BRIGHT) {
                if (mBrightness < 0) {
                    Log.d("MyGesture","??");
                    mBrightness = mActivity.getWindow().getAttributes().screenBrightness;
                }
                mBrightness = mActivity.getWindow().getAttributes().screenBrightness;
                Log.d("MyGesture",Float.toString(mBrightness));
                WindowManager.LayoutParams lpa = mActivity.getWindow().getAttributes();
                lpa.screenBrightness = mBrightness + (mOldY - y) / playerHeight;
                mBrightness = lpa.screenBrightness;
                if (lpa.screenBrightness > 1.0f)
                    lpa.screenBrightness = 1.0f;
                else if (lpa.screenBrightness < 0.01f)
                    lpa.screenBrightness = 0.01f;
                mGestureTextView.setText((int) (lpa.screenBrightness * 100) + "%");
                mActivity.getWindow().setAttributes(lpa);
            }

            firstScroll = false;// ?????????scroll???????????????????????????
            return true;
        }

        public void onLongPress(MotionEvent e) {
            Log.i("MyGesture", "onLongPress");
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            Log.i("MyGesture", "onFling");

            return true;
        }

    };

}
