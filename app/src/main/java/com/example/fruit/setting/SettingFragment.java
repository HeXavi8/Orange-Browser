package com.example.fruit.setting;

import android.app.AlertDialog;
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
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.fruit.MainActivity;
import com.example.fruit.R;
import com.example.fruit.collection.CollectionFragment;
import com.example.fruit.history.HistoryFragment;
import com.example.fruit.home.HomeFragment;
import com.example.fruit.login.LoginFragment;
import com.example.fruit.utils.Util;

public class SettingFragment extends Fragment implements View.OnClickListener, SettingsView {
    private ImageView mGoBack;
    private MainActivity mActivity;
    private Window mWindow;
    private TextView user_text;
    private RelativeLayout mPhone;
    private RelativeLayout mChangeUserName;
    private RelativeLayout mChangePassword;
    private RelativeLayout mLogOff;
    private LinearLayout mToCollection;
    private LinearLayout mToHistory;
    private Switch mNoHistory;

    private WindowManager.LayoutParams mLayoutParams;
    private PopupWindow mLogoutWindow;
    private PopupWindow mLogOffWindow;
    private PopupWindow mSettingNameWindow;
    private PopupWindow mSettingPasswordWindow;
    private SettingsPresenter mSettingsPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setting_fragment, container, false);
        mSettingsPresenter = new SettingsPresenter(this);
        mActivity = (MainActivity)getActivity();
        mGoBack = (ImageView)view.findViewById(R.id.setting_back);
        mPhone = (RelativeLayout)view.findViewById(R.id.setting_usr_phone);
        mChangeUserName = (RelativeLayout)view.findViewById(R.id.setting_change_usr_name);
        mChangePassword = (RelativeLayout)view.findViewById(R.id.setting_change_usr_password);
        mLogOff = (RelativeLayout)view.findViewById(R.id.close_account);
        mToCollection = (LinearLayout)view.findViewById(R.id.to_collection);
        mToHistory = (LinearLayout)view.findViewById(R.id.to_history);
        mNoHistory = (Switch)view.findViewById(R.id.wohen_mode_switch);
        mNoHistory.setChecked(Util.getInstance().getNoHistory());
        mNoHistory.setOnClickListener(this);
        mToCollection.setOnClickListener(this);
        mToHistory.setOnClickListener(this);
        mPhone.setOnClickListener(this);
        mChangeUserName.setOnClickListener(this);
        mChangePassword.setOnClickListener(this);
        mLogOff.setOnClickListener(this);
        mGoBack.setOnClickListener(this);
        mWindow =  mActivity.getWindow();
        mLayoutParams =mWindow.getAttributes();
        LinearLayout userField=view.findViewById(R.id.setting_user_field);
        Button logoutBtn=view.findViewById(R.id.logout_btn);
        logoutBtn.setOnClickListener(this);
        //如果已经登录，则显示 用户名称 、显示退出登录的按钮
        user_text=(TextView)view.findViewById(R.id.setting_user_text);

        Boolean isLogin= Util.getInstance().getLoginState();
        if(isLogin){
            mPhone.setVisibility(View.VISIBLE);
            mChangeUserName.setVisibility(View.VISIBLE);
            mChangePassword.setVisibility(View.VISIBLE);
            mLogOff.setVisibility(View.VISIBLE);
            TextView phoneNumber = view.findViewById(R.id.phone_number);
            String number = Util.getInstance().getUserName();
            StringBuilder toShow = new StringBuilder();
            for(int i = 0; i < number.length(); i++) {
                if (i < 2 || i >= 7) {
                    toShow.append(number.charAt(i));
                } else {
                    toShow.append('*');
                }
            }
            phoneNumber.setText(toShow.toString());
            String userName=Util.getInstance().getCustomizeName();
            user_text.setText(userName);
            logoutBtn.setVisibility(view.VISIBLE);
            user_text.setOnClickListener(this);
        }
        else{
            mPhone.setVisibility(View.GONE);
            mChangeUserName.setVisibility(View.GONE);
            mChangePassword.setVisibility(View.GONE);
            mLogOff.setVisibility(View.GONE);
            logoutBtn.setVisibility(View.GONE);
            userField.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActivity.replaceFragment(new LoginFragment());
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
            case R.id.setting_user_text:
            case R.id.setting_change_usr_name:
                changeUsername();
                break;
            case R.id.setting_change_usr_password:
                changePassword();
                break;
            case R.id.to_collection:
                if (Util.getInstance().getLoginState()) {
                    mActivity.replaceFragment(new CollectionFragment());
                } else {
                    mActivity.replaceFragment(new LoginFragment());
                }
                break;
            case R.id.to_history:
                mActivity.replaceFragment(new HistoryFragment());
                break;
            case R.id.close_account:
                logOff();
                break;
            case R.id.wohen_mode_switch:
                Util.getInstance().setNoHistory(!Util.getInstance().getNoHistory());
                break;
        }
    }

    private void logOff() {
        mLayoutParams.alpha=0.9f;
        mWindow.setAttributes(mLayoutParams);
        View contentView = LayoutInflater.from(mActivity)
                .inflate(R.layout.confirm_window, null);

        mLogOffWindow =new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true);
        mLogOffWindow.setBackgroundDrawable(new BitmapDrawable());
        mLogOffWindow.setOutsideTouchable(true);
        mLogOffWindow.setAnimationStyle(R.style.pop_window_anim_style);
        View rootView=LayoutInflater.from(mActivity).inflate(R.layout.setting_fragment,null);
        mLogOffWindow.showAtLocation(rootView, Gravity.BOTTOM,0,0);

        //退出按钮 和 取消按钮 事件
        TextView confirmText = contentView.findViewById(R.id.confirm_text);
        confirmText.setText(R.string.logoff_text);
        Button confirmBut=contentView.findViewById(R.id.confirm_btn);
        confirmBut.setText(R.string.logoff);
        Button cancelBut=contentView.findViewById(R.id.cancel_btn);
        confirmBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLogOffWindow.dismiss();
                mSettingsPresenter.deleteUser();
                mActivity.replaceFragment(new HomeFragment());
                Util.getInstance().setLoginState(false);
            }
        });
        cancelBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLogOffWindow.dismiss();
            }
        });
        mLogOffWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mLayoutParams.alpha = 1.0f;
                mWindow.setAttributes(mLayoutParams);
            }
        });
    }

//    点击退出登录，展示退出登录的pop
    private void showLogoutWindow(){
        mLayoutParams.alpha=0.9f;
        mWindow.setAttributes(mLayoutParams);
        View contentView = LayoutInflater.from(mActivity)
                .inflate(R.layout.confirm_window, null);

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
        TextView confirmText = contentView.findViewById(R.id.confirm_text);
        confirmText.setText(R.string.logout_text);
        Button confirmBut=contentView.findViewById(R.id.confirm_btn);
        confirmBut.setText(R.string.logout);
        Button cancelBut=contentView.findViewById(R.id.cancel_btn);
        confirmBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLogoutWindow.dismiss();
                mActivity.replaceFragment(new HomeFragment());
                Util.getInstance().setLoginState(false);
            }
        });
        cancelBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLogoutWindow.dismiss();
            }
        });
        mLogoutWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mLayoutParams.alpha = 1.0f;
                mWindow.setAttributes(mLayoutParams);
            }
        });
    }

    private void changePassword() {
        mLayoutParams.alpha=0.9f;
        mWindow.setAttributes(mLayoutParams);
        View contentView = LayoutInflater.from(mActivity)
                .inflate(R.layout.setting_password_window, null);

        mSettingPasswordWindow=new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true);
        mSettingPasswordWindow.setBackgroundDrawable(new BitmapDrawable());
        mSettingPasswordWindow.setOutsideTouchable(true);
        mSettingPasswordWindow.setAnimationStyle(R.style.pop_window_anim_style);
        View rootView=LayoutInflater.from(mActivity).inflate(R.layout.setting_fragment,null);
        mSettingPasswordWindow.showAtLocation(rootView,Gravity.CENTER,0,0);

        EditText oldPassword=contentView.findViewById(R.id.old_password);
        EditText newPassword = contentView.findViewById(R.id.new_password);

        mSettingPasswordWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mLayoutParams.alpha = 1.0f;
                mWindow.setAttributes(mLayoutParams);
            }
        });

        Button changeBtn=contentView.findViewById(R.id.change_btn);
        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPasswordInput = oldPassword.getText().toString();
                String newPasswordInput = newPassword.getText().toString();
                if (newPasswordInput.length() < 6 || newPasswordInput.length() > 12) {
                    Toast toast=Toast.makeText(mActivity,"密码长度为6~12位", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    mSettingsPresenter.changePassword(oldPasswordInput, newPasswordInput);
                    //弹窗关闭
                    mSettingPasswordWindow.dismiss();
                    mLayoutParams.alpha = 1.0f;
                    mWindow.setAttributes(mLayoutParams);
                }
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
                user_text.setText(newName);
                //修改数据库
                mSettingsPresenter.changeName(newName);
                //弹窗关闭
                mSettingNameWindow.dismiss();
                mLayoutParams.alpha = 1.0f;
                mWindow.setAttributes(mLayoutParams);
            }
        });
    }

    @Override
    public void showChangeName() {
        Toast toast=Toast.makeText(mActivity,"修改成功", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    public void showProfileAfterChange() {

    }

    @Override
    public void showCheckPasswordFalse() {
        Toast toast=Toast.makeText(mActivity,"密码错误", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    public void showCheckPasswordSuccess() {
        Toast toast=Toast.makeText(mActivity,"修改成功", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    public void showDeleteUser() {
        Util.getInstance().setLoginState(false);
        mActivity.replaceFragment(new HomeFragment());
    }
}
