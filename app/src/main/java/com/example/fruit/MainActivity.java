package com.example.fruit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Context;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.example.fruit.bean.QuickPage;
import com.example.fruit.collection.CollectionFragment;
import com.example.fruit.history.HistoryFragment;
import com.example.fruit.home.HomeFragment;
import com.example.fruit.login.LoginFragment;
import com.example.fruit.quick.QuickPageAdapter;
import com.example.fruit.search.SearchFragment;
import com.example.fruit.setting.SettingFragment;
import com.example.fruit.utils.Util;

import java.util.ArrayList;
import java.util.List;

import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MainView{
    private LinearLayout mTopSearch;
    private EditText mSearchUrl;
    private LinearLayout mNavigationBar;
    private LinearLayout mBack;
    private LinearLayout mForward;
    private LinearLayout mMenu;
    private LinearLayout mHome;
    private LinearLayout mPaging;
    private EditText mWebUrl;
    private PopupWindow mPopupWindow;
    private Window mWindow;
    private WindowManager.LayoutParams mLayoutParams;
    private String mCollectionURL;
    private String mCollectionTitle;

    private MainPresenter mMainPresenter;

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWindow = this.getWindow();
        mLayoutParams = mWindow.getAttributes();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        mTopSearch = (LinearLayout)findViewById(R.id.search_bar);
        mWebUrl = (EditText)findViewById(R.id.search_url);
        mWebUrl.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        mWebUrl.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    String content;
                    content = mWebUrl.getText().toString();
                    SearchFragment searchFragment = (SearchFragment)getFragmentManager()
                            .findFragmentById(R.id.show_page);
                    searchFragment.load(content);
                    return true;
                }
                return false;
            }
        });
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


        mMainPresenter = new MainPresenter(this);

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
            case R.id.add_collection_layout:
                clickAddCollection();
                mPopupWindow.dismiss();
                break;
            case R.id.my_collection:
                replaceFragment(new CollectionFragment());
                mTopSearch.setVisibility(View.GONE);
                mNavigationBar.setVisibility(View.GONE);
                mBack.setEnabled(false);
                mForward.setEnabled(false);
                mPopupWindow.dismiss();
                getImageBack().setImageDrawable(getResources().getDrawable(R.drawable.back_page));
                getImageForward().setImageDrawable(getResources().getDrawable(R.drawable.go_page));
                break;
            case R.id.my_history:
                replaceFragment(new HistoryFragment());
                mTopSearch.setVisibility(View.GONE);
                mNavigationBar.setVisibility(View.GONE);
                mBack.setEnabled(false);
                mForward.setEnabled(false);
                mPopupWindow.dismiss();
                getImageBack().setImageDrawable(getResources().getDrawable(R.drawable.back_page));
                getImageForward().setImageDrawable(getResources().getDrawable(R.drawable.go_page));
                break;
            case R.id.exit:
                finish();
                break;
            case R.id.my_setting:
                replaceFragment(new SettingFragment());
                mTopSearch.setVisibility(View.GONE);
                mNavigationBar.setVisibility(View.GONE);
                mBack.setEnabled(false);
                mForward.setEnabled(false);
                mPopupWindow.dismiss();
                getImageBack().setImageDrawable(getResources().getDrawable(R.drawable.back_page));
                getImageForward().setImageDrawable(getResources().getDrawable(R.drawable.go_page));
                break;
            case R.id.cancel_pop_window:
                mPopupWindow.dismiss();
                break;
            case R.id.login_msg:
                mPopupWindow.dismiss();
                replaceFragment(toWhichFragment(Util.getLoginState()));
                break;
            default:
        }
    }

    public void replaceFragment(Fragment fragment) {//fragment跳转函数
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.show_page, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public Fragment toWhichFragment(Boolean isLogin){
        if(isLogin){
            return new SettingFragment();
        }
        else{
            return new LoginFragment();
        }

    }

    /*
       1.判断登录，未登录跳到登录界面
       2.得到当前的fragment
       3.得到webview的url和title
       4.把url和title传入MainPresenter的addCollection函数中
    */
    private void clickAddCollection() {
        Boolean isLogin= Util.getInstance().getLoginState();//登录状态true or false

        if(isLogin){
            Fragment currentFragment = getFragmentManager().findFragmentById(R.id.show_page);//得到当前fragment
            if (currentFragment != null && currentFragment instanceof SearchFragment) {//判断是否为
                mCollectionTitle = ((SearchFragment) currentFragment).getCurrentWebTitle();//获得当前页面title
                mCollectionURL = ((SearchFragment) currentFragment).getCurrentWebURL();//获得当前页面url
                System.out.println("当前页面url和title" +mCollectionTitle+mCollectionURL);//检测获取结果
                mMainPresenter.addCollection(mCollectionURL,mCollectionTitle);//加入到collection中
            }

        }else{
            System.out.println("请先注册或登录");//需要先登录才能点击收藏
            Toast.makeText(this,"请先注册或登录",Toast.LENGTH_SHORT).show();
            mPopupWindow.dismiss();//菜单框收回
            mTopSearch.setVisibility(View.GONE);
            replaceFragment(new LoginFragment());//跳转到login界面
        }
    }

    @Override
    public void showAddCollection() {
        Toast.makeText(this, "已收藏", Toast.LENGTH_SHORT).show();
    }

    private void showPopWindow() {
        mLayoutParams.alpha = 0.9f;
        mWindow.setAttributes(mLayoutParams);
        View contentView = LayoutInflater.from(MainActivity.this)
                .inflate(R.layout.pop_window, null);
        mPopupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setAnimationStyle(R.style.pop_window_anim_style);
        LinearLayout addCollection = (LinearLayout) contentView.findViewById(R.id.add_collection_layout);
        LinearLayout myCollection = (LinearLayout) contentView.findViewById(R.id.my_collection);
        LinearLayout myHistory = (LinearLayout) contentView.findViewById(R.id.my_history);
        LinearLayout exit = (LinearLayout) contentView.findViewById(R.id.exit);
        LinearLayout mySetting = (LinearLayout) contentView.findViewById(R.id.my_setting);
        Button cancelPopWindow = (Button)contentView.findViewById(R.id.cancel_pop_window);
        LinearLayout loginMsg=contentView.findViewById(R.id.login_msg);
        TextView loginText=contentView.findViewById(R.id.msg_text);
        addCollection.setOnClickListener(this);
        myCollection.setOnClickListener(this);
        myHistory.setOnClickListener(this);
        exit.setOnClickListener(this);
        mySetting.setOnClickListener(this);
        cancelPopWindow.setOnClickListener(this);
        loginMsg.setOnClickListener(this);
        Fragment currentFragment = getFragmentManager().findFragmentById(R.id.show_page);
        if (currentFragment != null && currentFragment instanceof SearchFragment) {
            addCollection.setVisibility(View.VISIBLE);
        } else {
            addCollection.setVisibility(View.GONE);
        }
        View rootView = LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_main,
                null);
        mPopupWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mLayoutParams.alpha = 1.0f;
                mWindow.setAttributes(mLayoutParams);
            }
        });
        // 根据登录状态该改变 登录信息
        Boolean isLogin= Util.getInstance().getLoginState();
        if(isLogin){
            String userName=Util.getInstance().getCustomizeName();
            loginText.setText(userName);
        }
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
        mNavigationBar.setVisibility(View.VISIBLE);
    }


}