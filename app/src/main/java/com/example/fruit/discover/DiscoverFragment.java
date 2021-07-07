package com.example.fruit.discover;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.example.fruit.MainActivity;
import com.example.fruit.R;
import com.example.fruit.collection.CollectionFragment;
import com.example.fruit.history.HistoryFragment;
import com.example.fruit.login.LoginFragment;
import com.example.fruit.setting.SettingsPresenter;
import com.example.fruit.utils.Util;


public class DiscoverFragment extends Fragment {
    private ImageView mGoBack;
    private MainActivity mActivity;
    private Window mWindow;

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
        View view = inflater.inflate(R.layout.discover_fragment, container, false);
        //mSettingsPresenter = new SettingsPresenter(this);
        mActivity = (MainActivity) getActivity();
        mActivity.getNavigationBar().setVisibility(View.GONE);
        mActivity.getTopSearch().setVisibility(View.GONE);
        mGoBack = (ImageView) view.findViewById(R.id.discover_back);
        mGoBack.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mActivity.onBackPressed();
        }
        });

        return view;
    };

//    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.setting_back:
//                mActivity.onBackPressed();
//                break;
//        }
//    }



}
