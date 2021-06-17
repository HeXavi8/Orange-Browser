package com.example.fruit.setting;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.example.fruit.MainActivity;
import com.example.fruit.R;

public class SettingFragment extends Fragment implements View.OnClickListener {
    private ImageView mGoBack;
    private MainActivity mActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setting_fragment, container, false);
        mActivity = (MainActivity)getActivity();
        mGoBack = (ImageView)view.findViewById(R.id.setting_back);
        mGoBack.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setting_back:
                mActivity.onBackPressed();
                break;
        }
    }
}
