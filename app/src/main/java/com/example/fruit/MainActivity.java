package com.example.fruit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fruit.discover.DiscoverFragment;
import com.example.fruit.collection.CollectionFragment;
import com.example.fruit.history.HistoryFragment;
import com.example.fruit.home.HomeFragment;
import com.example.fruit.login.LoginFragment;
import com.example.fruit.search.SearchFragment;
import com.example.fruit.setting.SettingFragment;
import com.example.fruit.utils.Util;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MainView{
    private static final String TAG = "TEST";
    public static int mTitleBarHeight;

    private RelativeLayout mTopSearch;
    private EditText mSearchUrl;
    private LinearLayout mNavigationBar;
    private LinearLayout mBack;
    private LinearLayout mForward;
    private LinearLayout mMenu;
    private LinearLayout mHome;
    private ImageView mReload;
    private EditText mWebUrl;
    private PopupWindow mPopupWindow;
    private Window mWindow;
    private ImageView mDeleteEdit;
    private WindowManager.LayoutParams mLayoutParams;
    private androidx.fragment.app.Fragment myFragment;
    
    private String mCollectionURL;
    private String mCollectionTitle;
    private MainPresenter mMainPresenter;
    private Boolean mNightStyleBegin;
    @SuppressLint("JavascriptInterface")

//    public static void goHome(Context context) {
//        Intent intent = new Intent(context, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWindow = this.getWindow();
        mLayoutParams = mWindow.getAttributes();

        //After LOLLIPOP not translucent status bar
        mWindow.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //Then call setStatusBarColor.
        mWindow.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        mWindow.setStatusBarColor(getResources().getColor(R.color.subject_color));

        mDeleteEdit = (ImageView)findViewById(R.id.delete_edit);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        mTopSearch = (RelativeLayout)findViewById(R.id.search_bar);
        mWebUrl = (EditText)findViewById(R.id.search_url);
        mWebUrl.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        //??????????????????????????????id
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            mTitleBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        mDeleteEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebUrl.setText("");
            }
        });
        mWebUrl.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    mDeleteEdit.setVisibility(View.VISIBLE);
                }
                else{
                    mDeleteEdit.setVisibility(View.GONE);
                }
            }
        });
        mWebUrl.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    String content;
                    content = mWebUrl.getText().toString();
                    SearchFragment searchFragment = (SearchFragment)getFragmentManager()
                            .findFragmentById(R.id.show_page);
                    searchFragment.load(content);
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mWebUrl.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });
        mNavigationBar =(LinearLayout)findViewById(R.id.navigation_bar);
        mSearchUrl = (EditText)findViewById(R.id.search_url);
        mReload = (ImageView)findViewById(R.id.reload_page);
        mSearchUrl.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        mBack = (LinearLayout)findViewById(R.id.back);
        mForward = (LinearLayout)findViewById(R.id.forward);
        mMenu = (LinearLayout)findViewById(R.id.menu);
        mHome = (LinearLayout)findViewById(R.id.home);
        mBack.setEnabled(false);
        mBack.setEnabled(false);
        mReload.setOnClickListener(this);
        mMenu.setOnClickListener(this);
        mHome.setOnClickListener(this);
        replaceFragment(new HomeFragment());
        mMainPresenter = new MainPresenter(this);
        mNightStyleBegin=Util.getInstance().getNight();


        //??????????????????
        if (savedInstanceState != null) {

            myFragment = getSupportFragmentManager().getFragment(savedInstanceState, "fragment");

        }

        //????????????????????????
        setNightMode(!mNightStyleBegin);

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
//                replaceFragment(new HomeFragment());
                getFragmentManager().beginTransaction().replace(R.id.show_page, new HomeFragment()).commit();
                mTopSearch.setVisibility(View.GONE);
                getImageBack().setImageDrawable(getResources().getDrawable(R.drawable.back_page));
                getImageForward().setImageDrawable(getResources().getDrawable(R.drawable.go_page));
                break;
            case R.id.add_collection_layout:
                clickAddCollection();
                mPopupWindow.dismiss();
                break;
            case R.id.my_collection:
                if (Util.getInstance().getLoginState()) {
                    replaceFragment(new CollectionFragment());
                } else {
                    replaceFragment(new LoginFragment());
                }
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

            case R.id.discover:
                replaceFragment(new DiscoverFragment());
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
                mTopSearch.setVisibility(View.GONE);
                mNavigationBar.setVisibility(View.GONE);
                break;
            case R.id.reload_page:
                if(getFragmentManager().findFragmentById(R.id.show_page) instanceof SearchFragment){
                    String url = ((SearchFragment) getFragmentManager().findFragmentById(R.id.show_page)).getCurrentWebURL();
                    ((SearchFragment) getFragmentManager().findFragmentById(R.id.show_page)).load(url);
                }
                break;
            default:
        }
    }

    public void replaceFragment(Fragment fragment) {//fragment????????????
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        hideFragment = getFragmentManager().findFragmentById(R.id.show_page);
//        if(hideFragment==null){
//            Log.d(TAG,"no exist");
//            transaction.add(R.id.show_page,fragment);
//        }
//        else{
//            Log.d(TAG,"exist");
//            transaction.hide(hideFragment).add(R.id.show_page,fragment);
//        }
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
       1.??????????????????????????????????????????
       2.???????????????fragment
       3.??????webview???url???title
       4.???url???title??????MainPresenter???addCollection?????????
    */
    private void clickAddCollection() {
        Boolean isLogin= Util.getInstance().getLoginState();//????????????true or false

        if(isLogin){
            Fragment currentFragment = getFragmentManager().findFragmentById(R.id.show_page);//????????????fragment
            if (currentFragment != null && currentFragment instanceof SearchFragment) {//???????????????
                mCollectionTitle = ((SearchFragment) currentFragment).getCurrentWebTitle();//??????????????????title
                mCollectionURL = ((SearchFragment) currentFragment).getCurrentWebURL();//??????????????????url
                System.out.println("????????????url???title" +mCollectionTitle+mCollectionURL);//??????????????????
                mMainPresenter.addCollection(mCollectionURL,mCollectionTitle);//?????????collection???
            }

        }else{
            System.out.println("?????????????????????");//?????????????????????????????????
            Toast.makeText(this,"?????????????????????",Toast.LENGTH_SHORT).show();
            mPopupWindow.dismiss();//???????????????
            mTopSearch.setVisibility(View.GONE);
            replaceFragment(new LoginFragment());//?????????login??????
        }
    }

    @Override
    public void showAddCollection() {
        Toast.makeText(this, R.string.add_collection_successfully, Toast.LENGTH_SHORT).show();
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
        ImageView userImage=contentView.findViewById(R.id.user_image);
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
        // ??????????????????????????? ????????????
        Boolean isLogin= Util.getInstance().getLoginState();
        if(isLogin){
            String userName=Util.getInstance().getCustomizeName();
            loginText.setText(userName);
            
            //??????
            if (Util.getInstance().getProfile()!=null){
                Bitmap bitmap = BitmapFactory.decodeFile(Util.getInstance().getProfile());
                RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap); //??????RoundedBitmapDrawable??????
                roundedBitmapDrawable.setCornerRadius(bitmap.getWidth()); //????????????Radius????????????????????????
                roundedBitmapDrawable.setAntiAlias(true); //???????????????
                userImage.setImageDrawable(roundedBitmapDrawable); //????????????
            }
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

    public RelativeLayout getTopSearch() {
        return mTopSearch;
    }

    public LinearLayout getNavigationBar() {
        return mNavigationBar;
    }

    public EditText getSearchUrl() {
        return mSearchUrl;
    }

    public void setSearchUrl(String str){
        mSearchUrl.setText(str);
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

//    yx:fragment?????? dispatchTouchEVENT
    public interface MyTouchListener {
        void onTouchEvent(MotionEvent event);
    }



    // ??????MyTouchListener???????????????
    private List<MyTouchListener> myTouchListeners = new ArrayList<>();

    /**
     * ?????????Fragment??????getActivity()?????????????????????????????????????????????
     *
     * @param listener
     */
    public void registerMyTouchListener(MyTouchListener listener) {
        myTouchListeners.add(listener);
    }

    /**
     * ?????????Fragment??????getActivity()???????????????????????????????????????????????????
     *
     * @param listener
     */
    public void unRegisterMyTouchListener(MyTouchListener listener) {
        myTouchListeners.remove(listener);
    }

    /**
     * ????????????????????????????????????MyTouchListener?????????
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        Log.d(TAG,"Dispatch: "+ev.getAction());
        for (MyTouchListener listener : myTouchListeners) {
            listener.onTouchEvent(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    public void onBackPressed() {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.popBackStack();
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        Fragment currentFragment = getFragmentManager().findFragmentById(R.id.show_page);
//        if(hideFragment!=null)
//            Log.d(TAG,"exist");
//        if(hideFragment==currentFragment){
//            replaceFragment(new HomeFragment());
//            return;
//        }
//
//        transaction.remove(currentFragment).show(hideFragment).commit();
//
//        if(!(hideFragment instanceof HomeFragment || hideFragment instanceof SettingFragment
//        || hideFragment instanceof LoginFragment))
//            mTopSearch.setVisibility(View.VISIBLE);
//        mNavigationBar.setVisibility(View.VISIBLE);
    }

    /*????????????????????????????????????*/
    public void setNightMode(boolean nightMode) {
        //  ??????????????????//?????? ?????? ??????
        if(nightMode) {
            Log.d("tag", "setNightMode: ????????????");
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        }
        else{
            Log.d("tag", "setNightMode: ????????????");
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
       // finish();
        //startActivity(new Intent(MainActivity.this, MainActivity.class));
        //overridePendingTransition(R.anim.nigth_fade_in, R.anim.night_fade_out);


    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d("HHHH", "in onSaveInstanceState >> this:" + this + " outState:" + outState);
        super.onSaveInstanceState(outState);

//        if (true){
//            Log.d("hhhhh", "onSaveInstanceState: hhhhh");
//            getSupportFragmentManager().putFragment(outState, "SettingFragment",settingfra);
//
//        }
        if (myFragment != null) {
            Log.d("HHHH", "buzhidaodaodao");
            getSupportFragmentManager().putFragment(outState, "fragment", myFragment);
        }



    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d("HHH", "in onRestoreInstanceState >> this:" + this +
                " savedInstanceState:" + savedInstanceState);
        super.onRestoreInstanceState(savedInstanceState);
    }
}