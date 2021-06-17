package com.example.fruit.history;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.fruit.MainActivity;
import com.example.fruit.R;
import com.example.fruit.bean.History;
import com.example.fruit.search.SearchFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_fragment, container, false);
        mHistoryPresenter = new HistoryPresenter(this);
        mActivity = (MainActivity)getActivity();
        mGoBack = (ImageView)view.findViewById(R.id.setting_back);
        mDeleteAll = (Button)view.findViewById(R.id.delete_all_history);
        mDeleteSelected = (Button)view.findViewById(R.id.delete);
        mGoBack.setOnClickListener(this);
        mDeleteAll.setOnClickListener(this);
        mDeleteSelected.setOnClickListener(this);
        mHistoryList = (RecyclerView)view.findViewById(R.id.recyclerview);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mHistoryList.setLayoutManager(mLinearLayoutManager);
        mHistoryPresenter.getHistories();
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
                mHistoryItems.clear();
                mHistoryRecyclerviewAdapter.notifyDataSetChanged();
                mHistoryPresenter.deleteAllHistory();
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
}
