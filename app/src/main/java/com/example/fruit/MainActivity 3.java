package com.example.fruit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.fruit.collection.CollectionFragment;
import com.example.fruit.history.HistoryFragment;
import com.example.fruit.home.HomeFragment;
import com.example.fruit.search.SearchFragment;
import com.example.fruit.setting.SettingFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    public static SharedPreferences mHistory;
    private LinearLayout mTopSearch;
    private EditText mSearchUrl;
    private LinearLayout mNavigationBar;
    private LinearLayout mBack;
    private LinearLayout mForward;
    private LinearLayout mMenu;
    private LinearLayout mHome;
    private LinearLayout mPaging;
    private PopupWindow mPopupWindow;
    private String mURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        mHistory = getSharedPreferences("History", MODE_PRIVATE);
        mTopSearch = (LinearLayout)findViewById(R.id.search_bar);
        mNavigationBar =(LinearLayout)findViewById(R.id.navigation_bar);
        mSearchUrl = (EditText)findViewById(R.id.search_url);
        mSearchUrl.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        mBack = (LinearLayout)findViewById(R.id.back);
        mForward = (LinearLayout)findViewById(R.id.forward);
        mMenu = (LinearLayout)findViewById(R.id.menu);
        mHome = (LinearLayout)findViewById(R.id.home);
        mPaging = (LinearLayout)findViewById(R.id.paging);
        mBack.setEnabled(false);
        mBack.setEnabled(false);
        mMenu.setOnClickListener(this);
        mHome.setOnClickListener(this);
        mPaging.setOnClickListener(this);
        replaceFragment(new HomeFragment());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menu:
                showPopWindow();
                break;
            case R.id.home:
                mBack.setEnabled(false);
                mForward.setEnabled(false);
                replaceFragment(new HomeFragment());
                mTopSearch.setVisibility(View.GONE);
                getImageBack().setImageDrawable(getResources().getDrawable(R.drawable.back_page));
                getImageForward().setImageDrawable(getResources().getDrawable(R.drawable.go_page));
                break;
            case R.id.paging:
                mBack.setEnabled(false);
                mForward.setEnabled(false);
                mTopSearch.setVisibility(View.GONE);
                mPopupWindow.dismiss();
                break;
            case R.id.add_collection:
                break;
            case R.id.collection:
                replaceFragment(new CollectionFragment());
                mTopSearch.setVisibility(View.GONE);
                mBack.setEnabled(false);
                mForward.setEnabled(false);
                mPopupWindow.dismiss();
                getImageBack().setImageDrawable(getResources().getDrawable(R.drawable.back_page));
                getImageForward().setImageDrawable(getResources().getDrawable(R.drawable.go_page));
                break;
            case R.id.history:
                replaceFragment(new HistoryFragment());
                mTopSearch.setVisibility(View.GONE);
                mBack.setEnabled(false);
                mForward.setEnabled(false);
                mPopupWindow.dismiss();
                getImageBack().setImageDrawable(getResources().getDrawable(R.drawable.back_page));
                getImageForward().setImageDrawable(getResources().getDrawable(R.drawable.go_page));
                break;
            case R.id.exit:
                finish();
                break;
            case R.id.setting:
                replaceFragment(new SettingFragment());
                mTopSearch.setVisibility(View.GONE);
                mBack.setEnabled(false);
                mForward.setEnabled(false);
                mPopupWindow.dismiss();
                getImageBack().setImageDrawable(getResources().getDrawable(R.drawable.back_page));
                getImageForward().setImageDrawable(getResources().getDrawable(R.drawable.go_page));
                break;
            case R.id.cancel_pop_window:
                mPopupWindow.dismiss();
                break;
            default:
        }
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.show_page, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void showPopWindow() {
        View contentView = LayoutInflater.from(MainActivity.this)
                .inflate(R.layout.pop_window, null);
        mPopupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setAnimationStyle(R.style.pop_window_anim_style);
        ImageView addCollection = (ImageView)contentView.findViewById(R.id.add_collection);
        ImageView myCollection = (ImageView)contentView.findViewById(R.id.collection);
        ImageView myHistory = (ImageView)contentView.findViewById(R.id.history);
        ImageView exit = (ImageView)contentView.findViewById(R.id.exit);
        ImageView mySetting = (ImageView)contentView.findViewById(R.id.setting);
        Button cancelPopWindow = (Button)contentView.findViewById(R.id.cancel_pop_window);
        addCollection.setOnClickListener(this);
        myCollection.setOnClickListener(this);
        myHistory.setOnClickListener(this);
        exit.setOnClickListener(this);
        mySetting.setOnClickListener(this);
        cancelPopWindow.setOnClickListener(this);
        View rootView = LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_main,
                null);
        mPopupWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (mPopupWindow != null && mPopupWindow.isShowing()) {
                mPopupWindow.dismiss();
                return true;
            }
        }
        return false;
    }

    public String getURL() {
        if (mURL == null) {
            return "";
        } else {
            return mURL;
        }
    }

    public void setURL(String url) {
        mURL = url;
    }

    public LinearLayout getTopSearch() {
        return mTopSearch;
    }

    public LinearLayout getNavigationBar() {
        return mNavigationBar;
    }

    public EditText getSearchUrl() {
        return mSearchUrl;
    }

    public void backClick(final SearchFragment searchFragment) {
        mBack.setEnabled(true);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchFragment.back();
            }
        });
    }

    public void forwardClick(final SearchFragment searchFragment) {
        mForward.setEnabled(true);
        mForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchFragment.forward();
            }
        });
    }

    public ImageView getImageBack() {
        return findViewById(R.id.image_back);
    }

    public ImageView getImageForward() {
        return findViewById(R.id.image_forward);
    }

//    yx:fragment没有 dispatchTouchEVENT
public interface MyTouchListener {
    void onTouchEvent(MotionEvent event);
}

    // 保存MyTouchListener接口的列表
    private List<MyTouchListener> myTouchListeners = new ArrayList<>();

    /**
     * 提供给Fragment通过getActivity()方法来注册自己的触摸事件的方法
     *
     * @param listener
     */
    public void registerMyTouchListener(MyTouchListener listener) {
        myTouchListeners.add(listener);
    }

    /**
     * 提供给Fragment通过getActivity()方法来取消注册自己的触摸事件的方法
     *
     * @param listener
     */
    public void unRegisterMyTouchListener(MyTouchListener listener) {
        myTouchListeners.remove(listener);
    }

    /**
     * 分发触摸事件给所有注册了MyTouchListener的接口
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        for (MyTouchListener listener : myTouchListeners) {
            listener.onTouchEvent(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    public void onBackPressed() {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.popBackStack();
    }
}