package com.example.fruit.register;

import android.app.Fragment;
import android.content.res.ColorStateList;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.Nullable;

import com.example.fruit.MainActivity;
import com.example.fruit.R;
import com.example.fruit.home.HomeFragment;
import com.example.fruit.login.LoginFragment;

import com.example.fruit.dao.DBController;
import  com.example.fruit.register.RegisterPresenter;
import  com.example.fruit.register.RegisterView;

public class RegisterFragment extends Fragment {

    @Override
    public void onResume(){
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK){
                    getActivity().onBackPressed();
                    ((MainActivity)getActivity()).getNavigationBar().setVisibility(View.GONE);
                    return true;
                }
                return false;
            }
        });
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
      View view =inflater.inflate(R.layout.register_fragment,container,false);
        TextView toLogin = view.findViewById(R.id.to_login);
        ImageView backBtn=view.findViewById(R.id.back_btn);
        ((MainActivity)getActivity()).getNavigationBar().setVisibility(View.GONE);
        ((MainActivity)getActivity()).getTopSearch().setVisibility(View.GONE);
        EditText registerPhone=view.findViewById(R.id.user_phone);
        EditText registerPassword2=view.findViewById(R.id.user_password2);
        EditText registerPassword=view.findViewById(R.id.user_password);
        Button registerBut=view.findViewById(R.id.register_but);
        registerBut.setEnabled(false);

        final MainActivity activity=(MainActivity) getActivity();

//        ??????????????????????????????????????????
        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.replaceFragment((new LoginFragment()));
            }
        });

        //        ???????????????????????????????????????
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
                activity.getNavigationBar().setVisibility(View.GONE);
            }
        });

//?????? ????????? ???????????????
// ???????????????????????????????????????????????????????????????????????????????????????

        registerPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                /**
                 * ??????????????????????????????
                 * @param mobiles
                 * @return
                 * ?????????134???135???136???137???138???139???150???151???157(TD)???158???159???187???188
                 * ?????????130???131???132???152???155???156???185???186
                 * ?????????133???153???180???189??????1349?????????
                 * ????????????????????????????????????1?????????????????????3???5???8???????????????????????????0-9
                 */
                String phoneRegex = "[1][34578]\\d{9}" ;
                String inputPhone = registerPhone.getText().toString();
                if(TextUtils.isEmpty(inputPhone)&&"".equals(inputPhone)){
                    registerBut.setEnabled(false);
                    registerPhone.setError("??????????????????");
                }
                else if(!inputPhone.matches(phoneRegex)){
                    registerBut.setEnabled(false);
                    registerPhone.setError("?????????11????????????????????????");
                }
                else{
                    registerBut.setEnabled(true);
                }

            }
        });

//        ???????????????????????????
        registerPassword2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //
                String inputPassword = registerPassword.getText().toString();
                String inputPassword2 = registerPassword2.getText().toString();
                if(TextUtils.isEmpty(inputPassword2) && "".equals(inputPassword2)){
                    registerPassword2.setError("?????????????????????");
                }

            }
        });



        //??????????????????
        registerBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputPhone = registerPhone.getText().toString();
                String inputPassword=registerPassword.getText().toString();
                String inputPassword2=registerPassword2.getText().toString();
                if( "".equals(inputPhone)||"".equals(inputPassword)||"".equals(inputPassword2)){
                   Toast toast=Toast.makeText(activity,"??????????????????",Toast.LENGTH_SHORT);
                   toast.setGravity(Gravity.CENTER, 0, 0);
                   toast.show();
                }
                else if(inputPassword.length()<6||inputPassword.length()>12){
                    Toast toast=Toast.makeText(activity,"?????????????????????6~12",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                else if(!(inputPassword.equals(inputPassword2))){
                    Toast toast=Toast.makeText(activity,"????????????????????????????????????",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                // ???????????????
                else {
                    RegisterView registerView=new RegisterView() {
                        @Override
                        public void showRegisterSuccessfully() {
                            Toast toast=Toast.makeText(activity,"????????????", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            activity.replaceFragment((new LoginFragment()));
                        }

                        @Override
                        public void showUsernameExist() {
                            Toast toast=Toast.makeText(activity,"?????????????????????", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    };
                    RegisterPresenter registerPresenter=new RegisterPresenter(registerView);
                    registerPresenter.register(inputPhone,inputPassword);


                   }


                }
        });

        return view;
    }


}



