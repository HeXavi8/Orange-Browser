package com.example.fruit.history;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import com.example.fruit.MainActivity;
import com.example.fruit.R;
import com.example.fruit.bean.History;
import com.example.fruit.search.SearchFragment;
import com.example.fruit.utils.Util;

import java.util.ArrayList;
import java.util.List;


import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class HistoryFragment extends Fragment implements HistoryView, View.OnClickListener, OnItemClickListener {
    private HistoryPresenter mHistoryPresenter;
    private List<History> mHistoryItems = new ArrayList<>();
    private List<String> mSelected = new ArrayList<>();
    private List<History> mSelectedItems = new ArrayList<>();
    private HistoryAdapter mHistoryRecyclerviewAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private MainActivity mActivity;

    private RecyclerView mHistoryList;
    private Button mDeleteAll;
    private Button mDeleteSelected;
    private ImageView mGoBack;

    private Window mWindow;
    private WindowManager.LayoutParams mLayoutParams;
    private PopupWindow mDeleteAllHistoryWindow;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_fragment, container, false);
        mHistoryPresenter = new HistoryPresenter(this);
        mActivity = (MainActivity)getActivity();
        mGoBack = (ImageView)view.findViewById(R.id.setting_back);
        mDeleteAll = (Button)view.findViewById(R.id.delete_all_history);
        mDeleteSelected = (Button)view.findViewById(R.id.delete);
        mWindow =  mActivity.getWindow();
        mLayoutParams =mWindow.getAttributes();
        mGoBack.setOnClickListener(this);
        mDeleteAll.setOnClickListener(this);
        mDeleteSelected.setOnClickListener(this);
        mHistoryList = (RecyclerView)view.findViewById(R.id.recyclerview);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mHistoryList.setLayoutManager(mLinearLayoutManager);
        if (Util.getInstance().getNoHistory()) {
            mHistoryRecyclerviewAdapter= new HistoryAdapter(mHistoryItems, this);
            mHistoryList.setAdapter(mHistoryRecyclerviewAdapter);
        } else {
            mHistoryPresenter.getHistories();
        }
        return view;
    }

    @Override
    public void showAll(List<History> histories) {
        if (histories != null) {
            for (int i = histories.size()-1; i >= 0; i--) {
                mHistoryItems.add(histories.get(i));
            }
        }
        mHistoryRecyclerviewAdapter= new HistoryAdapter(mHistoryItems, this);
        mHistoryList.setAdapter(mHistoryRecyclerviewAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.delete_all_history:
                showDeleteAllWindow();

                break;
            case R.id.delete:
                getSelectedItems();
                mSelected.clear();
                mHistoryRecyclerviewAdapter.setShowCheckBox(false);
                mDeleteSelected.setVisibility(View.GONE);
                mDeleteAll.setVisibility(View.VISIBLE);
                refreshUI();
                mHistoryPresenter.deleteSelectedHistory(mSelectedItems);
                break;
            case R.id.setting_back:
                mActivity.onBackPressed();
                break;
            case R.id.confirm_btn:
                mDeleteAllHistoryWindow.dismiss();
                mHistoryItems.clear();
                mHistoryRecyclerviewAdapter.notifyDataSetChanged();
                mHistoryPresenter.deleteAllHistory();
            case R.id.cancel_btn:
                mDeleteAllHistoryWindow.dismiss();
        }
    }

    public void goToSearch(String url) {
        SearchFragment searchFragment = new SearchFragment();
        searchFragment.setURL(url);
        mActivity.replaceFragment(searchFragment);
    }

    private void refreshUI() {
        if (mHistoryRecyclerviewAdapter == null) {
            mHistoryRecyclerviewAdapter = new HistoryAdapter(mHistoryItems, this);
            mHistoryList.setAdapter(mHistoryRecyclerviewAdapter);
        } else {
            mHistoryRecyclerviewAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View view, int pos) {
        if (mSelected.contains(String.valueOf(pos))) {
            mSelected.remove(String.valueOf(pos));
        } else {
            mSelected.add(String.valueOf(pos));
        }
    }

    @Override
    public boolean onLongClick(View view, int pos) {
        refreshUI();
        mDeleteAll.setVisibility(View.GONE);
        mDeleteSelected.setVisibility(View.VISIBLE);
        return true;
    }

    private void getSelectedItems() {
        if (!mSelected.isEmpty()) {
            if (mSelectedItems.isEmpty()) {
                for (int i = 0; i < mSelected.size(); i++) {
                    int pos = Integer.parseInt(mSelected.get(i));
                    mSelectedItems.add(mHistoryItems.get(pos));
                }
                for (int i = 0; i < mSelectedItems.size(); i++) {
                    mHistoryItems.remove(mSelectedItems.get(i));
                }
            } else {
                mSelectedItems.clear();
            }
        }
    }

    private  void showDeleteAllWindow(){
        mLayoutParams.alpha=0.9f;
        mWindow.setAttributes(mLayoutParams);
        View contentView = LayoutInflater.from(mActivity)
                .inflate(R.layout.confirm_window, null);

        mDeleteAllHistoryWindow =new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true);
        mDeleteAllHistoryWindow.setBackgroundDrawable(new BitmapDrawable());
        mDeleteAllHistoryWindow.setOutsideTouchable(true);
        mDeleteAllHistoryWindow.setAnimationStyle(R.style.pop_window_anim_style);
        View rootView=LayoutInflater.from(mActivity).inflate(R.layout.history_fragment,null);
        mDeleteAllHistoryWindow.showAtLocation(rootView, Gravity.BOTTOM,0,0);

        //退出按钮 和 取消按钮 事件
        TextView confirmText = contentView.findViewById(R.id.confirm_text);
        confirmText.setText(R.string.delete_all_history_text);
        Button confirmBut=contentView.findViewById(R.id.confirm_btn);
        confirmBut.setText(R.string.delete_all);
        Button cancelBut=contentView.findViewById(R.id.cancel_btn);
        confirmBut.setOnClickListener(this);
        cancelBut.setOnClickListener(this);
        mDeleteAllHistoryWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mLayoutParams.alpha = 1.0f;
                mWindow.setAttributes(mLayoutParams);
            }
        });
    }
}
