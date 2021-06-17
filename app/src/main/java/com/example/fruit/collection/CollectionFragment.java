package com.example.fruit.collection;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fruit.R;

//public class CollectionFragment extends Fragment {
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.collection_fragment, container, false);
//        return view;
//    }

    public class CollectionFragment extends Fragment {
        private View view;//定义view用来设置fragment的layout
        public RecyclerView mCollectRecyclerView;//定义RecyclerView

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            //获取fragment的layout
            view = inflater.inflate(R.layout.collection_fragment, container, false);
            //对recycleview进行配置

            return view;

        }
    }