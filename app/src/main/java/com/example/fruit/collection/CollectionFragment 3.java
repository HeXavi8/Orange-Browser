package com.example.fruit.collection;

import android.app.Fragment;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fruit.R;

import com.example.fruit.collection.CollectRecycleAdapter;

import java.util.ArrayList;

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
        //定义以goodsentity实体类为对象的数据集合
        private ArrayList<GoodsEntity> goodsEntityList = new ArrayList<GoodsEntity>();
        //自定义recyclerveiw的适配器
        private CollectRecycleAdapter mCollectRecyclerAdapter;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            //获取fragment的layout
            view = inflater.inflate(R.layout.collection_fragment, container, false);
            //对recycleview进行配置

            //模拟数据
            initData();
            initRecyclerView();
            return view;
        }

        /**
         * TODO 模拟数据
         */
        private void initData() {
            for (int i=0;i<10;i++){
                GoodsEntity goodsEntity=new GoodsEntity();
                goodsEntity.setGoodsName("模拟数据"+i);
                goodsEntity.setGoodsPrice("100"+i);
                goodsEntityList.add(goodsEntity);
            }
        }

        /**
         * TODO 对recycleview进行配置
         */

        private void initRecyclerView() {
            //获取RecyclerView
            mCollectRecyclerView=(RecyclerView)view.findViewById(R.id.collect_recyclerView);
            //创建adapter
            mCollectRecyclerAdapter = new CollectRecycleAdapter(getActivity(), goodsEntityList);
            //给RecyclerView设置adapter

            //设置layoutManager,可以设置显示效果，是线性布局、grid布局，还是瀑布流布局
            //参数是：上下文、列表方向（横向还是纵向）、是否倒叙
            mCollectRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            //设置item的分割线
            mCollectRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
            //RecyclerView中没有item的监听事件，需要自己在适配器中写一个监听事件的接口。参数根据自定义
            mCollectRecyclerAdapter.setOnItemClickListener(new CollectRecycleAdapter.OnItemClickListener() {
                @Override
                public void OnItemClick(View view, GoodsEntity data) {
                    //此处进行监听事件的业务处理
                    Toast.makeText(getActivity(),"item",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onItemLongClick(View view, GoodsEntity data) {
                    //此处进行监听事件的业务处理
                    Toast.makeText(getActivity(),"我是Long item",Toast.LENGTH_SHORT).show();
                }
            });
            mCollectRecyclerView.setAdapter(mCollectRecyclerAdapter);
        }

    }

