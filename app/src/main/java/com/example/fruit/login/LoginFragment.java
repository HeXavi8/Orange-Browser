package com.example.fruit.login;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fruit.MainActivity;
import com.example.fruit.R;
import com.example.fruit.bean.User;
import com.example.fruit.home.HomeFragment;
import com.example.fruit.register.RegisterFragment;
import  com.example.fruit.login.LoginView;
import  com.example.fruit.login.LoginPresenter;
import  com.example.fruit.utils.Util;

import java.util.ArrayList;
import java.util.List;

public class LoginFragment extends Fragment implements LoginView {
    private MainActivity activity;
    private TextView toRegister;
    private EditText userPhone;
    private EditText userPassword;
    private ImageView backBtn;
    private String username;
    private String password;
    private LoginPresenter loginPresenter;
    private MainActivity.MyTouchListener myTouchListener;

    @Override
    public void onResume(){
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK){
                    activity.unRegisterMyTouchListener(myTouchListener);
                    activity.onBackPressed();
                    return true;
                }
                return false;
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);
        loginPresenter = new LoginPresenter(this);
        TextView toRegister = view.findViewById(R.id.to_register);
        ((MainActivity)getActivity()).getNavigationBar().setVisibility(View.GONE);
        ((MainActivity)getActivity()).getTopSearch().setVisibility(View.GONE);
        EditText userPhone =view.findViewById(R.id.user_phone);
        EditText userPassword=view.findViewById(R.id.user_password);
        backBtn=view.findViewById(R.id.back_btn);
        Button loginBtn=view.findViewById(R.id.login_btn);

        loginBtn.setEnabled(false);
//        ?????????????????????:???????????????utils/???????????????????????????????????????
        activity = (MainActivity) getActivity();
        //?????? ???????????????????????????????????????
        myTouchListener = new MainActivity.MyTouchListener() {
            @Override
            public void onTouchEvent(MotionEvent event) {
                hideInputWhenTouchOtherView(getActivity(), event, getExcludeTouchHideInputViews());
            }
        };
        activity.registerMyTouchListener(myTouchListener);

//        ?????????????????? ???????????????
        toRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.unRegisterMyTouchListener(myTouchListener);
                activity.replaceFragment(new RegisterFragment());
            }
        });
       // ?????????????????????????????????
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.unRegisterMyTouchListener(myTouchListener);
                activity.onBackPressed();
            }
        });
//        ????????????
        userPhone.setOnFocusChangeListener(new android.view.View.
                OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // ???????????????????????????????????????
                        String inputUserPhone = userPhone.getText().toString();
                        String phoneRegex = "[1][34578]\\d{9}" ;
                        if(TextUtils.isEmpty(inputUserPhone) && "".equals(inputUserPhone)) {
                        userPhone.setError("??????????????????");
                    } else if(!inputUserPhone.matches(phoneRegex)) {
                        userPhone.setError("?????????11????????????????????????");
                    }
                        else{
                            loginBtn.setEnabled(true);
                        }
                }
            }
        });

        //??????????????????
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputPhone = userPhone.getText().toString();
                String inputPassword=userPassword.getText().toString();
                username = inputPhone;
                password = inputPassword;
                if( "".equals(inputPhone)||"".equals(inputPassword)){
                    Toast toast=Toast.makeText(activity,"??????????????????",Toast.LENGTH_SHORT);
                    //toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }

                // ???????????????
                else {
                   loginPresenter.login(inputPhone,inputPassword);

                }
            }
        });

        return view;
    }

    @Override
    public void showLoginSuccessfully(User user) {
        //????????????
        Util.getInstance().setLoginState(true);
        Util.getInstance().setUserName(username);
        Util.getInstance().setCustomizeName(user.getCustomizeName());
        Util.getInstance().setProfile(user.getProfile());
        //???????????????
        activity.replaceFragment((new HomeFragment()));
        activity.getNavigationBar().setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoginFailed() {
        Toast toast=Toast.makeText(activity,"??????????????????", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

    }



    //??????????????????view ??????????????????????????????????????????????????????
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
     * ???????????????View??????????????????
     * @param activity
     * @param ev
     * @param excludeViews  ????????????View?????????????????????????????????
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



    //??????????????? view
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
    //??????????????????
    public static final boolean isShouldHideInput(View v, MotionEvent event){
//???????????????????????????View
        if (v != null && (v instanceof EditText)){
            return !isTouchView(v, event);
        }
        return false;
    }


}
