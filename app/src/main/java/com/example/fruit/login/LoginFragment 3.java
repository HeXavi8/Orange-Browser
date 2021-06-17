package com.example.fruit.login;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fruit.MainActivity;
import com.example.fruit.R;
import com.example.fruit.home.HomeFragment;
import com.example.fruit.register.RegisterFragment;

import java.util.ArrayList;
import java.util.List;

public class LoginFragment extends Fragment {
    private MainActivity activity;
    private TextView toRegister;
    private EditText userPhone;
    private EditText userPassword;
    private ImageView backBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);
        toRegister = (TextView)view.findViewById(R.id.to_register);
        userPhone = (EditText)view.findViewById(R.id.user_phone);
        userPassword = (EditText)view.findViewById(R.id.user_password);
        backBtn = (ImageView)view.findViewById(R.id.back_btn);
        activity = (MainActivity) getActivity();
        //点击 空白处，收起键盘的事件绑定
        MainActivity.MyTouchListener myTouchListener = new MainActivity.MyTouchListener() {
            @Override
            public void onTouchEvent(MotionEvent event) {
                hideInputWhenTouchOtherView(getActivity(), event, getExcludeTouchHideInputViews());
            }
        };
        activity.registerMyTouchListener(myTouchListener);

//        点击注册按钮 ，跳转事件
        toRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.unRegisterMyTouchListener(myTouchListener);
                activity.replaceFragment(new RegisterFragment());
            }
        });
//        点击回退按钮，回到首页
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.unRegisterMyTouchListener(myTouchListener);
                activity.onBackPressed();
            }
        });
//        失去焦点
        userPhone.setOnFocusChangeListener(new android.view.View.
                OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // 此处为失去焦点时的处理内容
                        String inputUserPhone = userPhone.getText().toString();
                        String phoneRegex = "[1][34578]\\d{9}" ;
                        if(TextUtils.isEmpty(inputUserPhone) && "".equals(inputUserPhone)) {
                        userPhone.setError("请输入手机号");
                    } else if(!inputUserPhone.matches(phoneRegex)) {
                        userPhone.setError("请输入11位数的正确手机号");
                    }
                }
            }
        });

        return view;
    }



    //这个是排除的view 除了点击他们。其他时候都要收起软键盘
    private List getExcludeTouchHideInputViews() {
        List views = new ArrayList<>();
        View view = View.inflate(getContext(), R.layout.login_fragment, null);
        EditText userPhone =view.findViewById(R.id.user_phone);
        EditText userPassword=view.findViewById(R.id.user_password);
        views.add(userPhone);
        views.add(userPassword);
        return views;
    }

    /**
     * 当点击其他View时隐藏软键盘
     * @param activity
     * @param ev
     * @param excludeViews  点击这些View不会触发隐藏软键盘动作
     */
    public static final void hideInputWhenTouchOtherView(Activity activity, MotionEvent ev, List<View> excludeViews){
        if (ev.getAction() == MotionEvent.ACTION_DOWN){
            if (excludeViews != null && !excludeViews.isEmpty()){
                for (int i = 0; i < excludeViews.size(); i++){
                    if (isTouchView(excludeViews.get(i), ev)){
                        return;
                    }
                }
            }
            View v = activity.getCurrentFocus();
            if (isShouldHideInput(v, ev)){
                InputMethodManager inputMethodManager = (InputMethodManager)
                        activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager != null){
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
    }



    //是否点击到 view
    public static final boolean isTouchView(View view, MotionEvent event){
        if (view == null || event == null){
            return false;
        }
        int[] leftTop = {0, 0};
        view.getLocationInWindow(leftTop);
        int left = leftTop[0];
        int top = leftTop[1];
        int bottom = top + view.getHeight();
        int right = left + view.getWidth();
        if (event.getRawX() > left && event.getRawX() < right
                && event.getRawY() > top && event.getRawY() < bottom){
            return true;
        }
        return false;
    }
    //是否收起键盘
    public static final boolean isShouldHideInput(View v, MotionEvent event){
//这里可以添加自定义View
        if (v != null && (v instanceof EditText)){
            return !isTouchView(v, event);
        }
        return false;
    }


}
