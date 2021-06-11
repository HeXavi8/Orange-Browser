package com.example.fruit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.example.fruit.collection.CollectionFragment;
import com.example.fruit.history.HistoryFragment;
import com.example.fruit.home.HomeFragment;
import com.example.fruit.setting.SettingFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private LinearLayout mTopSearch;
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
        mTopSearch = (LinearLayout)findViewById(R.id.search_bar);
        mBack = (LinearLayout)findViewById(R.id.back);
        mForward = (LinearLayout)findViewById(R.id.forward);
        mMenu = (LinearLayout)findViewById(R.id.menu);
        mHome = (LinearLayout)findViewById(R.id.home);
        mPaging = (LinearLayout)findViewById(R.id.paging);
        mBack.setOnClickListener(this);
        mForward.setOnClickListener(this);
        mMenu.setOnClickListener(this);
        mHome.setOnClickListener(this);
        mPaging.setOnClickListener(this);
        mBack.setEnabled(false);
        mForward.setEnabled(false);
        replaceFragment(new HomeFragment());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                break;
            case R.id.forward:
                break;
            case R.id.menu:
                showPopWindow();
                break;
            case R.id.home:
                replaceFragment(new HomeFragment());
                mTopSearch.setVisibility(View.GONE);
                break;
            case R.id.paging:
                break;
            case R.id.add_collection:
                break;
            case R.id.collection:
                replaceFragment(new CollectionFragment());
                break;
            case R.id.history:
                replaceFragment(new HistoryFragment());
                break;
            case R.id.exit:
                break;
            case R.id.setting:
                replaceFragment(new SettingFragment());
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
                mBack.getHeight()+250,
                true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setAnimationStyle(R.style.pop_window_anim_style);
        ImageView addCollection = (ImageView)contentView.findViewById(R.id.add_collection);
        ImageView myCollection = (ImageView)contentView.findViewById(R.id.collection);
        ImageView myHistory = (ImageView)contentView.findViewById(R.id.history);
        ImageView exit = (ImageView)contentView.findViewById(R.id.exit);
        ImageView mySetting = (ImageView)contentView.findViewById(R.id.setting);
        addCollection.setOnClickListener(this);
        myCollection.setOnClickListener(this);
        myHistory.setOnClickListener(this);
        exit.setOnClickListener(this);
        mySetting.setOnClickListener(this);
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
}