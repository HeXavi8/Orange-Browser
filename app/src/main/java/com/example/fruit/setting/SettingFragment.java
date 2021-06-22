package com.example.fruit.setting;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.fruit.MainActivity;
import com.example.fruit.R;
import com.example.fruit.home.HomeFragment;
import com.example.fruit.login.LoginFragment;
import com.example.fruit.utils.Util;

public class SettingFragment extends Fragment implements View.OnClickListener {
    private ImageView mGoBack;
    private MainActivity mActivity;
    private Window mWindow;
    private WindowManager.LayoutParams mLayoutParams;
    private PopupWindow mLogoutWindow;
    private PopupWindow mSettingNameWindow;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setting_fragment, container, false);
        mActivity = (MainActivity)getActivity();
        mGoBack = (ImageView)view.findViewById(R.id.setting_back);
        mGoBack.setOnClickListener(this);
        mWindow =  mActivity.getWindow();
        mLayoutParams =mWindow.getAttributes();
        LinearLayout userField=view.findViewById(R.id.setting_user_field);
        Button logoutBtn=view.findViewById(R.id.logout_btn);
        logoutBtn.setOnClickListener(this);
        //如果已经登录，则显示 用户名称 、显示退出登录的按钮
        TextView user_text=view.findViewById(R.id.setting_user_text);

        Boolean isLogin= Util.getInstance().getLoginState();
        if(isLogin){
            String userName=Util.getInstance().getCustomizeName();
            user_text.setText(userName);
            logoutBtn.setVisibility(view.VISIBLE);
            user_text.setOnClickListener(this);
        }
        else{
            logoutBtn.setVisibility(View.GONE);
            userField.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    replaceFragment(new LoginFragment());
                }
            });
        }
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setting_back:
                mActivity.onBackPressed();
                break;
            case R.id.logout_btn:
                showLogoutWindow();
                break;
            case R.id.confirm_logout_btn:
                mLogoutWindow.dismiss();
                replaceFragment(new HomeFragment());
                Util.getInstance().setLoginState(false);
                break;
            case R.id.cancel_btn:
                mLogoutWindow.dismiss();
                break;
            case R.id.setting_user_text:
                changeUsername();
                break;

        }
    }
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.show_page, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
//    点击退出登录，展示退出登录的pop
    private  void showLogoutWindow(){
        mLayoutParams.alpha=0.9f;
        mWindow.setAttributes(mLayoutParams);
        View contentView = LayoutInflater.from(mActivity)
                .inflate(R.layout.logout_window, null);

        mLogoutWindow =new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true);
        mLogoutWindow.setBackgroundDrawable(new BitmapDrawable());
        mLogoutWindow.setOutsideTouchable(true);
        mLogoutWindow.setAnimationStyle(R.style.pop_window_anim_style);
        View rootView=LayoutInflater.from(mActivity).inflate(R.layout.setting_fragment,null);
        mLogoutWindow.showAtLocation(rootView, Gravity.BOTTOM,0,0);

        //退出按钮 和 取消按钮 事件
        Button confirmBut=contentView.findViewById(R.id.confirm_logout_btn);
        Button cancelBut=contentView.findViewById(R.id.cancel_btn);
        confirmBut.setOnClickListener(this);
        cancelBut.setOnClickListener(this);
        mLogoutWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mLayoutParams.alpha = 1.0f;
                mWindow.setAttributes(mLayoutParams);
            }
        });
    }

//    点击登录状态下的用户名，可以进行一个修改昵称的操作
    private void changeUsername(){
        mLayoutParams.alpha=0.9f;
        mWindow.setAttributes(mLayoutParams);
        View contentView = LayoutInflater.from(mActivity)
                .inflate(R.layout.setting_name_window, null);

        mSettingNameWindow=new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true);
        mSettingNameWindow.setBackgroundDrawable(new BitmapDrawable());
        mSettingNameWindow.setOutsideTouchable(true);
        mSettingNameWindow.setAnimationStyle(R.style.pop_window_anim_style);
        View rootView=LayoutInflater.from(mActivity).inflate(R.layout.setting_fragment,null);
        mSettingNameWindow.showAtLocation(rootView,Gravity.CENTER,0,0);

        //输入框默认为用户原先的名字
        EditText userName=contentView.findViewById(R.id.user_name);
        String oldName=Util.getInstance().getCustomizeName();
        userName.setText(oldName);
        userName .setSelection(userName .getText().length());

        mSettingNameWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mLayoutParams.alpha = 1.0f;
                mWindow.setAttributes(mLayoutParams);
            }
        });


        //点击修改按钮，对用户名进行修改
        Button changeBtn=contentView.findViewById(R.id.change_btn);
        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName=userName.getText().toString();
                Util.getInstance().setCustomizeName(newName);
                SettingsView settingsView=new SettingsView() {
                    @Override
                    public void showChangeName() {
                        Toast toast=Toast.makeText(mActivity,"修改成功", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                };
                SettingsPresenter settingsPresenter=new SettingsPresenter(settingsView);
                //修改数据库
                settingsPresenter.changeName(newName);
                replaceFragment(new SettingFragment());
                //弹窗关闭
                mSettingNameWindow.dismiss();
                mLayoutParams.alpha = 1.0f;
                mWindow.setAttributes(mLayoutParams);


            }
        });



    }
}
