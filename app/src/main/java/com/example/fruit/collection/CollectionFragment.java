package com.example.fruit.collection;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import com.example.fruit.MainActivity;
import com.example.fruit.R;
import com.example.fruit.bean.Collection;

import com.example.fruit.search.SearchFragment;

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

public class CollectionFragment extends Fragment implements CollectionView, View.OnClickListener, OnItemClickListener {
    private CollectionPresenter mCollectionPresenter;
    private List<Collection> mCollectionItems = new ArrayList<>();
    private List<String> mSelected = new ArrayList<>();
    private List<Collection> mSelectedItems = new ArrayList<>();
    private CollectionAdapter mCollectionRecyclerviewAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private MainActivity mActivity;

    private RecyclerView mCollectionList;
    private Button mDeleteAll;
    private Button mDeleteSelected;
    private ImageView mGoBack;

    private Window mWindow;
    private WindowManager.LayoutParams mLayoutParams;
    private PopupWindow mDeleteAllCollectionWindow;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //获取fragment的layout
        View view = inflater.inflate(R.layout.collection_fragment, container, false);//定义view用来设置fragment的layout
        mCollectionPresenter = new CollectionPresenter(this);
        mActivity = (MainActivity)getActivity();
        mWindow =  mActivity.getWindow();
        mLayoutParams = mWindow.getAttributes();
        mGoBack = (ImageView)view.findViewById(R.id.setting_back);
        mDeleteAll = (Button)view.findViewById(R.id.delete_all_collection);
        mDeleteSelected = (Button)view.findViewById(R.id.delete);
        mGoBack.setOnClickListener(this);
        mDeleteAll.setOnClickListener(this);
        mDeleteSelected.setOnClickListener(this);
        mCollectionList = (RecyclerView)view.findViewById(R.id.recyclerview);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mCollectionList.setLayoutManager(mLinearLayoutManager);
        mCollectionPresenter.getCollections();


        return view;

    }


    @Override
    public void showAllCollection(List<Collection> collections) {//显示所有收藏
        if (collections != null) {
            for (int i = collections.size()-1; i >= 0; i--) {
                mCollectionItems.add(collections.get(i));
            }
        }
        mCollectionRecyclerviewAdapter= new CollectionAdapter(mCollectionItems, this);
        mCollectionList.setAdapter(mCollectionRecyclerviewAdapter);
    }

    @Override
    public void onClick(View view) {//点击操作
        switch (view.getId()) {
            case R.id.delete_all_collection:
                showDeleteAllWindow();
                break;
            case R.id.delete:
                getSelectedItems();
                mSelected.clear();
                mCollectionRecyclerviewAdapter.setShowCheckBox(false);
                mDeleteSelected.setVisibility(View.GONE);
                mDeleteAll.setVisibility(View.VISIBLE);
                refreshUI();
                mCollectionPresenter.deleteSelectedCollection(mSelectedItems);
                break;
            case R.id.setting_back:
                mActivity.onBackPressed();
                break;
            case R.id.confirm_btn:
                mDeleteAllCollectionWindow.dismiss();
                mCollectionItems.clear();
                mCollectionRecyclerviewAdapter.notifyDataSetChanged();
                mCollectionPresenter.deleteAllCollection();
                break;
            case R.id.cancel_btn:
                mDeleteAllCollectionWindow.dismiss();

        }
    }


    public void goToSearch(String url) {//点击跳转到该页面
        SearchFragment searchFragment = new SearchFragment();
        searchFragment.setURL(url);
        mActivity.replaceFragment(searchFragment);
    }

    private void refreshUI() {//更新界面
        if (mCollectionRecyclerviewAdapter == null) {
            mCollectionRecyclerviewAdapter = new CollectionAdapter(mCollectionItems, this);
            mCollectionList.setAdapter(mCollectionRecyclerviewAdapter);
        } else {
            mCollectionRecyclerviewAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onClick(View view, int pos) {//点击
        if (mSelected.contains(String.valueOf(pos))) {
            mSelected.remove(String.valueOf(pos));//移除
        } else {
            mSelected.add(String.valueOf(pos));//添加
        }
    }

    @Override
    public boolean onLongClick(View view, int pos) {//长按
        refreshUI();
        mDeleteAll.setVisibility(View.GONE);//不能全部删除
        mDeleteSelected.setVisibility(View.VISIBLE);//显示checkbox
        return true;
    }

    private void getSelectedItems() {//获取到所有点击的items
        if (!mSelected.isEmpty()) {
            if (mSelectedItems.isEmpty()) {
                for (int i = 0; i < mSelected.size(); i++) {
                    int pos = Integer.parseInt(mSelected.get(i));
                    mSelectedItems.add(mCollectionItems.get(pos));
                }
                for (int i = 0; i < mSelectedItems.size(); i++) {
                    mCollectionItems.remove(mSelectedItems.get(i));
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

        mDeleteAllCollectionWindow =new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true);
        mDeleteAllCollectionWindow.setBackgroundDrawable(new BitmapDrawable());
        mDeleteAllCollectionWindow.setOutsideTouchable(true);
        mDeleteAllCollectionWindow.setAnimationStyle(R.style.pop_window_anim_style);
        View rootView=LayoutInflater.from(mActivity).inflate(R.layout.collection_fragment,null);
        mDeleteAllCollectionWindow.showAtLocation(rootView, Gravity.BOTTOM,0,0);

        //退出按钮 和 取消按钮 事件
        TextView confirmText = contentView.findViewById(R.id.confirm_text);
        confirmText.setText(R.string.delete_all_collection_text);
        Button confirmBut=contentView.findViewById(R.id.confirm_btn);
        confirmBut.setText(R.string.delete_all);
        Button cancelBut=contentView.findViewById(R.id.cancel_btn);
        confirmBut.setOnClickListener(this);
        cancelBut.setOnClickListener(this);
        mDeleteAllCollectionWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mLayoutParams.alpha = 1.0f;
                mWindow.setAttributes(mLayoutParams);
            }
        });
    }

}