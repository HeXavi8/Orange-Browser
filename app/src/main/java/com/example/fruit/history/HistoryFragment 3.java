package com.example.fruit.history;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.fruit.R;
import com.example.fruit.bean.History;

import java.util.ArrayList;
import java.util.List;


import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;

public class HistoryFragment extends Fragment implements HistoryView, View.OnClickListener {
    private HistoryPresenter mHistoryPresenter;
    private List<HistoryItem> mHistoryItems = new ArrayList<>();
    private HistoryAdapter mHistoryRecyclerviewAdapter;

    private RecyclerView mHistoryList;
    private Button mDeleteAll;
    private Button mDeleteSelected;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_fragment, container, false);
        mHistoryPresenter = new HistoryPresenter(this);
        mDeleteAll = (Button)view.findViewById(R.id.delete_all_history);
        mDeleteSelected = (Button)view.findViewById(R.id.delete);
        mDeleteAll.setOnClickListener(this);
        mDeleteSelected.setOnClickListener(this);
        mHistoryList = (RecyclerView)view.findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mHistoryList.setLayoutManager(linearLayoutManager);
        mHistoryPresenter.getHistories();
        return view;
    }

    @Override
    public void showAll(List<History> histories) {
        if (histories != null) {
            for (int i = 0; i < histories.size(); i++) {
                HistoryItem historyItem = new HistoryItem();
                historyItem.setTitle(histories.get(i).getTitle());
                historyItem.setUrl(histories.get(i).getUrl());
                mHistoryItems.add(historyItem);
            }
        }
        mHistoryRecyclerviewAdapter= new HistoryAdapter(mHistoryItems);
        mHistoryList.setAdapter(mHistoryRecyclerviewAdapter);
    }

    @Override
    public void showDeleteSelected() {

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
                break;
        }
    }
}
