package com.example.fruit.setting;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import static android.app.Activity.RESULT_OK;


import android.content.Intent;

import android.content.pm.PackageManager;
import android.database.Cursor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;

import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.bumptech.glide.load.ImageHeaderParserUtils;
import com.bumptech.glide.load.model.ResourceLoader;
import com.example.fruit.BuildConfig;
import com.example.fruit.MainActivity;
import com.example.fruit.R;
import com.example.fruit.collection.CollectionFragment;
import com.example.fruit.history.HistoryFragment;
import com.example.fruit.home.HomeFragment;
import com.example.fruit.login.LoginFragment;
import com.example.fruit.utils.Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.datatype.DatatypeFactory;

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
    private Switch mNightStyle;
    private ImageView mUserImage;

    private WindowManager.LayoutParams mLayoutParams;
    private PopupWindow mLogoutWindow;
    private PopupWindow mLogOffWindow;
    private PopupWindow mSettingNameWindow;
    private PopupWindow mSettingPasswordWindow;
    private PopupWindow mSettingImageWindow;
    private PopupWindow mSettingLogOffWindow;
    private SettingsPresenter mSettingsPresenter;


    //????????????
    private Bitmap head;// ??????Bitmap
    private static String path  = Environment.getExternalStorageDirectory().toString();
    private String newImagePath;
    private  Uri uritempFile;

    @Override
    public void onResume(){
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK){
                    mActivity.onBackPressed();
                    return true;
                }
                return false;
            }
        });
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setting_fragment, container, false);
        mSettingsPresenter = new SettingsPresenter(this);
        mActivity = (MainActivity)getActivity();
        mActivity.getNavigationBar().setVisibility(View.GONE);
        mActivity.getTopSearch().setVisibility(View.GONE);
        mGoBack = (ImageView)view.findViewById(R.id.setting_back);
        mPhone = (RelativeLayout)view.findViewById(R.id.setting_usr_phone);
        mChangeUserName = (RelativeLayout)view.findViewById(R.id.setting_change_usr_name);
        mChangePassword = (RelativeLayout)view.findViewById(R.id.setting_change_usr_password);
        mLogOff = (RelativeLayout)view.findViewById(R.id.close_account);
        mToCollection = (LinearLayout)view.findViewById(R.id.to_collection);
        mToHistory = (LinearLayout)view.findViewById(R.id.to_history);
        mNoHistory = (Switch)view.findViewById(R.id.wohen_mode_switch);
        mUserImage=(ImageView)view.findViewById(R.id.setting_user_photo);
        mNightStyle=(Switch)view.findViewById(R.id.dark_mode_switch);
        mNoHistory.setChecked(Util.getInstance().getNoHistory());
        mNightStyle.setChecked(!Util.getInstance().getNight());
        mNoHistory.setOnClickListener(this);
        mToCollection.setOnClickListener(this);
        mToHistory.setOnClickListener(this);
        mPhone.setOnClickListener(this);
        mChangeUserName.setOnClickListener(this);
        mChangePassword.setOnClickListener(this);
        mLogOff.setOnClickListener(this);
        mGoBack.setOnClickListener(this);
        mNightStyle.setOnClickListener(this);
        mWindow =  mActivity.getWindow();
        mLayoutParams =mWindow.getAttributes();
        LinearLayout userField=view.findViewById(R.id.setting_user_field);
        Button logoutBtn=view.findViewById(R.id.logout_btn);
        logoutBtn.setOnClickListener(this);


        //?????????????????????????????? ???????????? ??????????????????????????????

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
            // ??????????????????????????????
            if (Util.getInstance().getProfile()!=null){
                Bitmap bitmap = BitmapFactory.decodeFile(Util.getInstance().getProfile());
                RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap); //??????RoundedBitmapDrawable??????
                roundedBitmapDrawable.setCornerRadius(bitmap.getWidth()); //????????????Radius????????????????????????
                roundedBitmapDrawable.setAntiAlias(true); //???????????????
                mUserImage.setImageDrawable(roundedBitmapDrawable); //????????????
            }
            //???????????????????????????????????????????????????
            mUserImage.setOnClickListener(this);

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


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            //????????????????????????
            for (String str : permissions) {
                if (mActivity.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //????????????
                    this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                }
            }
            if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.CAMERA}, 1001);
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                builder.detectFileUriExposure();
            }
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
            case R.id.setting_user_photo:
                changeUserImage();
                break;
            case R.id.dark_mode_switch:
                mActivity.setNightMode(Util.getInstance().getNight());
                Util.getInstance().setNight(!Util.getInstance().getNight());

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

        //???????????? ??? ???????????? ??????
        TextView confirmText = contentView.findViewById(R.id.confirm_text);
        confirmText.setText(R.string.logoff_text);
        Button confirmBut=contentView.findViewById(R.id.confirm_btn);
        confirmBut.setText(R.string.logoff);
        Button cancelBut=contentView.findViewById(R.id.cancel_btn);
        confirmBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLogOffWindow.dismiss();
                logOffByConfirm();
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

    private void logOffByConfirm() {
        mLayoutParams.alpha=0.9f;
        mWindow.setAttributes(mLayoutParams);
        View contentView = LayoutInflater.from(mActivity)
                .inflate(R.layout.confirm_password_window, null);

        mSettingLogOffWindow=new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true);
        mSettingLogOffWindow.setBackgroundDrawable(new BitmapDrawable());
        mSettingLogOffWindow.setOutsideTouchable(true);
        mSettingLogOffWindow.setAnimationStyle(R.style.pop_window_anim_style);
        View rootView=LayoutInflater.from(mActivity).inflate(R.layout.setting_fragment,null);
        mSettingLogOffWindow.showAtLocation(rootView,Gravity.CENTER,0,0);

        mSettingLogOffWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mLayoutParams.alpha = 1.0f;
                mWindow.setAttributes(mLayoutParams);
            }
        });

        EditText userPassword = contentView.findViewById(R.id.user_password);
        Button yesBtn=contentView.findViewById(R.id.yes_btn);
        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = userPassword.getText().toString();
                //???????????????
                mSettingsPresenter.deleteUser(password);
                //????????????
                mSettingLogOffWindow.dismiss();
                mLayoutParams.alpha = 1.0f;
                mWindow.setAttributes(mLayoutParams);

            }
        });
    }

//    ??????????????????????????????????????????pop
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
        //???????????? ??? ???????????? ??????
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
                mActivity.getNavigationBar().setVisibility(View.VISIBLE);
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
                    Toast toast=Toast.makeText(mActivity,"???????????????6~12???", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    mSettingsPresenter.changePassword(oldPasswordInput, newPasswordInput);
                    //????????????
                    mSettingPasswordWindow.dismiss();
                    mLayoutParams.alpha = 1.0f;
                    mWindow.setAttributes(mLayoutParams);
                }
            }
        });
    }

//    ???????????????????????????????????????????????????????????????????????????
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

        //???????????????????????????????????????
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


        //?????????????????????????????????????????????
        Button changeBtn=contentView.findViewById(R.id.change_btn);
        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName=userName.getText().toString();
                Util.getInstance().setCustomizeName(newName);
                user_text.setText(newName);
                //???????????????
                mSettingsPresenter.changeName(newName);
                //????????????
                mSettingNameWindow.dismiss();
                mLayoutParams.alpha = 1.0f;
                mWindow.setAttributes(mLayoutParams);

            }
        });
    }

    @Override
    public void showChangeName() {
        Toast toast=Toast.makeText(mActivity,"????????????", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    public void showProfileAfterChange() {
        //??????????????????
        Toast toast=Toast.makeText(mActivity,"????????????", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    public void showCheckPasswordFalse() {
        Toast toast=Toast.makeText(mActivity,"????????????", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    public void showCheckPasswordSuccess() {
        Toast toast=Toast.makeText(mActivity,"????????????", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    public void showDeleteUser() {
        mActivity.getNavigationBar().setVisibility(View.VISIBLE);
        Util.getInstance().setLoginState(false);
        mActivity.replaceFragment(new HomeFragment());
    }

    public void changeUserImage(){
        mLayoutParams.alpha=0.9f;
        mWindow.setAttributes(mLayoutParams);
        View contentView = LayoutInflater.from(mActivity)
                .inflate(R.layout.setting_image_window, null);

        mSettingImageWindow=new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true);
        mSettingImageWindow.setBackgroundDrawable(new BitmapDrawable());
        mSettingImageWindow.setOutsideTouchable(true);
        mSettingImageWindow.setAnimationStyle(R.style.pop_window_anim_style);
        View rootView=LayoutInflater.from(mActivity).inflate(R.layout.setting_fragment,null);
        mSettingImageWindow.showAtLocation(rootView,Gravity.CENTER,0,0);
        mSettingImageWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mLayoutParams.alpha = 1.0f;
                mWindow.setAttributes(mLayoutParams);
            }
        });
        TextView fromPhotoBtn=contentView.findViewById(R.id.from_photo_btn);
        TextView takePthotoBtn=contentView.findViewById(R.id.take_photo_btn);
        TextView cancelChangeBtn=contentView.findViewById(R.id.cancel_change_btn);
        fromPhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Intent.ACTION_PICK, null);
                //????????????
                intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent1, 1);
                mSettingImageWindow.dismiss();
                mLayoutParams.alpha = 1.0f;
                mWindow.setAttributes(mLayoutParams);


            }
        });

        takePthotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri uri= FileProvider.getUriForFile(mActivity, BuildConfig.APPLICATION_ID + ".provider",new File(Environment.getExternalStorageDirectory(), "head.jpg"));
                intent2.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                intent2.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivityForResult(intent2, 2);// ??????ForResult??????
                mSettingImageWindow.dismiss();
                mLayoutParams.alpha = 1.0f;
                mWindow.setAttributes(mLayoutParams);

            }
        });
        cancelChangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSettingImageWindow.dismiss();
                mLayoutParams.alpha = 1.0f;
                mWindow.setAttributes(mLayoutParams);

            }
        });


    }
    @Override
    public void onStart() {
        super.onStart();
        Bitmap bt=null;
        if (newImagePath!=null){
            bt = getBitmap(newImagePath);
        };
        if (bt != null) {
            @SuppressWarnings("deprecation")
            Drawable drawable = new BitmapDrawable(bt);
            mUserImage.setImageDrawable(drawable);
        } else {

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:

                if (resultCode == RESULT_OK) {
                    cropPhoto(data.getData());// ????????????
                }

                break;
            case 2:

                if (resultCode == RESULT_OK) {
                    Uri uri= FileProvider.getUriForFile(mActivity, BuildConfig.APPLICATION_ID + ".provider",new File(Environment.getExternalStorageDirectory(), "head.jpg"));
                    cropPhoto(uri);// ????????????

                }

                break;
            case 3:
                if(data!=null) {
                    try {

                        //???????????????????????????

                        InputStream fileInputStream = getContext().getContentResolver().openInputStream(uritempFile);
                        head = BitmapFactory.decodeStream(fileInputStream); //??????Bitmap
                        if (head != null) {
                            /**
                             * ?????????????????????
                             */
                            try {
                                setPicToView(head);// ???????????????
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                        mUserImage.setImageBitmap(head);
                        mSettingsPresenter.changeProfile(newImagePath);
                        Util.getInstance().setProfile(newImagePath);
                        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), head); //??????RoundedBitmapDrawable??????
                        roundedBitmapDrawable.setCornerRadius(head.getWidth()); //????????????Radius????????????????????????
                        roundedBitmapDrawable.setAntiAlias(true); //???????????????
                        mUserImage.setImageDrawable(roundedBitmapDrawable); //????????????
                        mActivity.replaceFragment(new SettingFragment());

                    }catch(FileNotFoundException e) {
                        e.printStackTrace(); }

                }
                break;
            default:
                break;

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    // ?????????????????????????????????????????? ?????????????????????
    private Bitmap getBitmap(String pathString) {
        Bitmap bitmap = null;
        try {
            File file = new File(pathString);
            if (file.exists()) {
                bitmap = BitmapFactory.decodeFile(pathString);
                //????????????
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
        return bitmap;
    }

    /**
     * ???????????????????????????
     *
     * @param uri
     */
    public void cropPhoto(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY ??????????????????
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY ?????????????????????
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
        //intent.putExtra("return-data", true);
        uritempFile = Uri.parse("file://" + "/"+Environment.getExternalStorageDirectory().getPath()+"/"+System.currentTimeMillis() + ".jpg");

        intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
        // ??????????????????
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
        startActivityForResult(intent, 3);

    }

    /**
     * ???????????????
     *
     * @param mBitmap
     */

    private void setPicToView(Bitmap mBitmap) throws FileNotFoundException {
        // ??????sdcard?????????
       path =Environment.getExternalStorageDirectory().getAbsolutePath();
       newImagePath=path+"/head.png";
//        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // ??????sd????????????
//            Log.d("dd", "setPicToView:sd???????????? ");
//            return;
//        }
        FileOutputStream b = null;
        File file = new File(path);
        if(!file.exists())
        {
            file.mkdirs();
            //???????????????
        }
        File imageFile =  new File(file, "head.png");// ????????????

        try {
            imageFile.createNewFile();
            b = new FileOutputStream(imageFile);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// ?????????????????????
            b.flush();
            b.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



}
