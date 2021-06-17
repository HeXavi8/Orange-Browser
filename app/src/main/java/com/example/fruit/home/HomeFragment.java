package com.example.fruit.home;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.fruit.MainActivity;
import com.example.fruit.R;
import com.example.fruit.login.LoginFragment;
import com.example.fruit.search.SearchFragment;

public class HomeFragment extends Fragment {
    private ImageView mUser;
    private EditText mSearchContent;
    private String mContent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        mUser = (ImageView) view.findViewById(R.id.to_login);
        mSearchContent = (EditText)view.findViewById(R.id.search_content);
        mSearchContent.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        final MainActivity activity = (MainActivity) getActivity();
        mUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.replaceFragment(new LoginFragment());
            }
        });
        mSearchContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    mContent = mSearchContent.getText().toString();
                    SearchFragment searchFragment = new SearchFragment();
                    searchFragment.setURL(mContent);
                    activity.replaceFragment(searchFragment);
                    return true;
                }
                return false;
            }
        });
        return view;
    }
}
