package com.example.fruit.history;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.fruit.R;
//import com.bumptech.glide.Glide;
import com.example.fruit.history.HistoryRecyclerviewAdapter;
import com.example.fruit.history.HistoryItem;
//import com.zm.recyclerviewtest.utils.DashlineItemDivider;
//import com.zm.recyclerviewtest.utils.SomeUtils;

import java.time.Instant;
import java.util.ArrayList;




import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.annotation.Nullable;

import com.example.fruit.MainActivity;
import com.example.fruit.R;

public class HistoryFragment extends Fragment {
    private Button btn_add,btn_delete,btn_update,btn_query,btn_all; //增删改查
    private TextView tv_count;
    private EditText et_search;

    private RecyclerView recyclerview;

    private HistoryRecyclerviewAdapter adapter;
    private ArrayList<HistoryItem> list,searchList,tempList;

    private HistoryItem selectedHistory;

    private int addIndex = 0;//添加的数据序号
    private int checkNumber = 0;//勾选的数量
    //////////////////////////////////////////////

    private WebView mHistoryRes;
    private String mURL;
    private MainActivity mActivity;
    @Nullable

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_fragment, container, false);
        btn_delete = (Button)view.findViewById(R.id.btn_delete);
        //btn_query = (Button)view.findViewById(R.id.btn_query);
        btn_all = (Button)view.findViewById(R.id.btn_all);
        et_search = (EditText)view.findViewById(R.id.et_search);
        recyclerview = (RecyclerView)view.findViewById(R.id.recyclerview);

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "delete",Toast.LENGTH_LONG).show();
            }
        });
        //btn_update.setOnClickListener(this);
//        btn_query.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getActivity(), "query",Toast.LENGTH_LONG).show();
//            }
//        });
        btn_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "all",Toast.LENGTH_LONG).show();
            }
        });


        //初始化一些虚拟数据
        tempList = new ArrayList<HistoryItem>();//临时保存数据的集合，查询时会用到
        list = new ArrayList<HistoryItem>();//数据列表集合
        list.add(new HistoryItem("百度","www.baidu.com"));
        list.add(new HistoryItem("网易","www.163.com"));


//        //设置item点击事件,初始化之后要重新设置点击监听事件等，不设置可能不起作用
//        adapter.setItemClickListener(new HistoryRecyclerviewAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(int position) {
//                selectedHistory = list.get(position);
//                //选中状态置为相反的
//                selectedHistory.setSelected(!selectedHistory.isSelected());
//                //刷新点击选中的那个，也可以全部刷新  adapter.notifyDataSetChanged();
//                adapter.notifyItemChanged(position);
//            }
//        });

//        //设置选中个数监听
//        adapter.setCountCheckBoxListener(new HistoryRecyclerviewAdapter.CountCheckBoxListener() {
//            @Override
//            public void countNumber(int checkNum, int total) {
//                checkNumber = checkNum;
//                tv_count.setText(checkNumber+"/"+total);
//            }
//        });

//        //item长按事件监听
//        adapter.setItemLongClickListener(new HistoryRecyclerviewAdapter.OnItemLongClickListener() {
//            @Override
//            public void onItemLongClick(int position) {
//                Toast.makeText(getActivity(), "longClick",Toast.LENGTH_LONG).show();
//                //showUpdateDialog(MainActivity.this,position);
//            }
//        });


        return view;
    }



//删除操作
private void doDelete()
{
    //未勾选有退出
    if(checkNumber==0)
    {
        //Toast.makeText(HistoryFragment.this,"未勾选",Toast.LENGTH_SHORT).show();
        return;
    }
    //删除勾选的item
    HistoryItem item;
    //这里要倒序删除，如果顺序可能会报错java.util.ConcurrentModificationException
    for(int i=list.size()-1;i>=0;i--)
    {
        item = list.get(i);
        if(item.isSelected())
        {
            list.remove(i);
        }
    }
    //刷新列表
    adapter.notifyDataSetChanged();
    //执行删除后把全选按钮状态恢复
    btn_all.setText("全选");
    //删除后把统计个数恢复
    tv_count.setText("0/"+list.size());

    //这里是为了解决执行查找再删除后的bug,把临时集合里面的数据也删除
    for(int i=tempList.size()-1;i>=0;i--)
    {
        item = tempList.get(i);
        if(item.isSelected())
        {
            tempList.remove(i);
        }
    }
}


//全选操作
private void doSelectAll()
{
        if("全选".equals(btn_all.getText().toString()))
        {
            btn_all.setText("取消");
            for(HistoryItem stu:list)
            {
                stu.setSelected(true);
            }
            adapter.notifyDataSetChanged();

        }else {
            btn_all.setText("全选");
            for(HistoryItem stu:list)
            {
                stu.setSelected(false);
            }
            adapter.notifyDataSetChanged();
        }
}





}

//
//
///**
// * 主界面，用recyclerview实现简单的增删改查，这里只用到LinearLayoutManager，其他布局的没用到
// * 包括glide加载图片等
// */
//public class HistoryFragment extends AppCompatActivity implements View.OnClickListener {
//    //private Button btn_LinearLayoutManager,btn_GridLayoutManager,btn_StaggeredGridLayoutManager;//三种布局切换
//    private Button btn_add,btn_delete,btn_update,btn_query,btn_all; //增删改查
//    private TextView tv_count;
//    private EditText et_search;
//
//    private RecyclerView recyclerview;
//
//    private HistoryRecyclerviewAdapter adapter;
//    private ArrayList<HistoryItem> list,searchList,tempList;
//
//    private HistoryItem selectedHistory;
//
//    private int addIndex = 0;//添加的数据序号
//    private int checkNumber = 0;//勾选的数量
//    //private Instant Glide;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.history_fragment);
//
//        //初始化控件
////        btn_LinearLayoutManager = findViewById(R.id.btn_LinearLayoutManager);
////        btn_GridLayoutManager = findViewById(R.id.btn_GridLayoutManager);
////        btn_StaggeredGridLayoutManager = findViewById(R.id.btn_StaggeredGridLayoutManager);
//
////        btn_add = findViewById(R.id.btn_add);
////        btn_delete = findViewById(R.id.btn_delete);
////        btn_update = findViewById(R.id.btn_update);
////        btn_query = findViewById(R.id.btn_query);
//        btn_all = findViewById(R.id.btn_all);
//
////        tv_count = findViewById(R.id.tv_count);
//
//        et_search = findViewById(R.id.et_search);
//
//        recyclerview = findViewById(R.id.recyclerview);
//
//        //为控件设置点击监听
////        btn_LinearLayoutManager.setOnClickListener(this);
////        btn_GridLayoutManager.setOnClickListener(this);
////        btn_StaggeredGridLayoutManager.setOnClickListener(this);
//        //btn_add.setOnClickListener(this);
//        btn_delete.setOnClickListener(this);
//        //btn_update.setOnClickListener(this);
//        btn_query.setOnClickListener(this);
//        btn_all.setOnClickListener(this);
//
//
//        //监听搜索输入框的内容变化，可实时查找（要把下面的ifif判断注释或者去掉才行）
////        et_search.addTextChangedListener(new TextWatcher() {
////            @Override
////            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
////                //这里只对当搜索框中的东西为空时才去执行实时查找操作
////                String keyword=et_search.getText().toString().trim();
////                //把这个if判断注释或者去掉就可以实时查找了
////                if(keyword.isEmpty())
////                {
////                    doQuery(keyword);
////                }
////            }
////            @Override
////            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
////            }
////            @Override
////            public void afterTextChanged(Editable arg0) {
////            }
////        });
//
//        //初始化一些虚拟数据
//        tempList = new ArrayList<HistoryItem>();//临时保存数据的集合，查询时会用到
//        list = new ArrayList<HistoryItem>();//数据列表集合
//        list.add(new HistoryItem("百度","www.baidu.com"));
//        //list.add(new HistoryItem("张三",25,"",false,R.mipmap.hjr));
//        //list.add(new HistoryItem("李四",23,"http://img2.imgtn.bdimg.com/it/u=1396521707,1973816781&fm=26&gp=0.jpg",false,R.mipmap.huli));
//        //list.add(new HistoryItem("王五",20,"http://img2.imgtn.bdimg.com/it/u=1031599332,2025124400&fm=26&gp=0.jpg",false,R.mipmap.nazha));
//
//        //初始化recyclerview
//        //initRecyclerView(R.id.btn_LinearLayoutManager);
//
//    }
//
//    /**
//     * 根据id切换recyclerview的模式
//     * @param btnId
//     */
//    private void initRecyclerView(int btnId)
//    {
//        switch (btnId)
//        {
////            case R.id.btn_LinearLayoutManager:
////                //设置recyclerview
////                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
////                linearLayoutManager.setOrientation(RecyclerView.VERTICAL);//RecyclerView.HORIZONTAL
////                recyclerview.setLayoutManager(linearLayoutManager);
////
////                adapter = new MyRecyclerviewAdapter(this,list,R.layout.layout_students_list_item);
////                recyclerview.setAdapter(adapter);
////                //添加分割线
////                recyclerview.addItemDecoration(new DashlineItemDivider());
////                break;
////            case R.id.btn_GridLayoutManager://切换GridLayoutManager
////                GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
////                recyclerview.setLayoutManager(gridLayoutManager);
////                //如果三种布局一样不需要重新适配adapter
////                adapter = new MyRecyclerviewAdapter(this,list,R.layout.layout_students_grid_item);
////                recyclerview.setAdapter(adapter);
////                break;
////            case R.id.btn_StaggeredGridLayoutManager://切换StaggeredGridLayoutManager
////                StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
////                recyclerview.setLayoutManager(staggeredGridLayoutManager);
////                //如果三种布局一样不需要重新适配adapter
////                adapter = new MyRecyclerviewAdapter(this,list,R.layout.layout_students_grid_item);
////                recyclerview.setAdapter(adapter);
////                break;
//        }
//
//
//        //设置item点击事件,初始化之后要重新设置点击监听事件等，不设置可能不起作用
//        adapter.setItemClickListener(new HistoryRecyclerviewAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(int position) {
//                selectedHistory = list.get(position);
//                //选中状态置为相反的
//                selectedHistory.setSelected(!selectedHistory.isSelected());
//                //刷新点击选中的那个，也可以全部刷新  adapter.notifyDataSetChanged();
//                adapter.notifyItemChanged(position);
//            }
//        });
//
//        //设置选中个数监听
//        adapter.setCountCheckBoxListener(new HistoryRecyclerviewAdapter.CountCheckBoxListener() {
//            @Override
//            public void countNumber(int checkNum, int total) {
//                checkNumber = checkNum;
//                tv_count.setText(checkNumber+"/"+total);
//            }
//        });
//
//
//        //item长按事件监听
////        adapter.setItemLongClickListener(new HistoryRecyclerviewAdapter.OnItemLongClickListener() {
////            @Override
////            public void onItemLongClick(int position) {
////                showUpdateDialog(HistoryFragment.this,position);
////            }
////        });
//
//    }
//
//
//    @Override
//    public void onClick(View view) {
//        switch (view.getId())
//        {
////            case R.id.btn_LinearLayoutManager://切换LinearLayoutManager
////                initRecyclerView(R.id.btn_LinearLayoutManager);
////                break;
////            case R.id.btn_GridLayoutManager://切换GridLayoutManager
////                initRecyclerView(R.id.btn_GridLayoutManager);
////                break;
////            case R.id.btn_StaggeredGridLayoutManager://切换StaggeredGridLayoutManager
////                initRecyclerView(R.id.btn_StaggeredGridLayoutManager);
////                break;
////
////            case R.id.btn_add:
////                //数据添加一个假数据
////                list.add(new HistoryItem("新加"+(++addIndex),25,"",false,getHeadImageId(addIndex)));
////                //刷新列表
////                adapter.notifyDataSetChanged();
////                break;
////            case R.id.btn_delete:
////                doDelete();
////                break;
////            case R.id.btn_update:
////
////                break;
////            case R.id.btn_query:
////                //查找，目前只能查找一个关键字，要实时查找可到 et_search.addTextChangedListener里面去修改
////                doQuery(et_search.getText().toString().trim());
////                break;
//            case R.id.btn_all:
//                doSelectAll();
//                break;
//        }
//    }
//
//    //删除操作
//    private void doDelete()
//    {
//        //未勾选有退出
//        if(checkNumber==0)
//        {
//            Toast.makeText(HistoryFragment.this,"未勾选",Toast.LENGTH_SHORT).show();
//            return;
//        }
//        //删除勾选的item
//        HistoryItem item;
//        //这里要倒序删除，如果顺序可能会报错java.util.ConcurrentModificationException
//        for(int i=list.size()-1;i>=0;i--)
//        {
//            item = list.get(i);
//            if(item.isSelected())
//            {
//                list.remove(i);
//            }
//        }
//        //刷新列表
//        adapter.notifyDataSetChanged();
//        //执行删除后把全选按钮状态恢复
//        btn_all.setText("全选");
//        //删除后把统计个数恢复
//        tv_count.setText("0/"+list.size());
//
//        //这里是为了解决执行查找再删除后的bug,把临时集合里面的数据也删除
//        for(int i=tempList.size()-1;i>=0;i--)
//        {
//            item = tempList.get(i);
//            if(item.isSelected())
//            {
//                tempList.remove(i);
//            }
//        }
//    }
//
//
//    //全选操作
//    private void doSelectAll()
//    {
//        if("全选".equals(btn_all.getText().toString()))
//        {
//            btn_all.setText("取消");
//            for(HistoryItem stu:list)
//            {
//                stu.setSelected(true);
//            }
//            adapter.notifyDataSetChanged();
//
//        }else {
//            btn_all.setText("全选");
//            for(HistoryItem stu:list)
//            {
//                stu.setSelected(false);
//            }
//            adapter.notifyDataSetChanged();
//        }
//    }
//
//    /**
//     * 设置虚拟数据头像资源Id,默认9 张图片
//     * @param position
//     */
////    private int getHeadImageId(int position)
////    {
////        int imgId = R.mipmap.hjr;
////        //适配不同的图片，可以替换成网络上的图片url
////        if(position%9 == 0)
////        {
////            imgId = R.mipmap.hjr;
////        }else if(position%9 == 1)
////        {
////            imgId = R.mipmap.hjr2;
////        }else if(position%9 == 2)
////        {
////            imgId = R.mipmap.yu;
////        }else if(position%9 == 3)
////        {
////            imgId = R.mipmap.hjr3;
////        }else if(position%9 == 4)
////        {
////            imgId = R.mipmap.paidax;
////        }
////        else if(position%9 == 5 )
////        {
////            imgId = R.mipmap.nazha;
////        }else if(position%9 == 6)
////        {
////            imgId = R.mipmap.han;
////        }else if(position%9 == 7)
////        {
////            imgId = R.mipmap.yanjing;
////        }else if(position%9 == 8)
////        {
////            imgId = R.mipmap.huli;
////        }
////        return imgId;
////    }
//
//    /**
//     * 显示修改的dialog对话框
//     * @param context
//     * @param position
//     */
////    private void showUpdateDialog(Context context, final int position)
////    {
////        final HistoryItem selStudent = list.get(position);
////        AlertDialog.Builder builder = new AlertDialog.Builder(context);
////        View view = View.inflate(context, R.layout.layout_update_dialog,null);
////        ImageView iv_headImg = view.findViewById(R.id.iv_headImg);
////        final EditText et_name = view.findViewById(R.id.et_name);
////        final EditText et_age = view.findViewById(R.id.et_age);
////        //设置头像，这里设置出的头像不是按照原来的可能，这里可能有错，要按照selStudent的来设置要修改一下才行
////        //如果设置了网上图片url的就按照网上的设置，否则设置本地的图片
////        if(selStudent.getHeadImgUrl()!=null && !selStudent.getHeadImgUrl().isEmpty())
////        {
////            Glide.with(context).load(selStudent.getHeadImgUrl()).into(iv_headImg);
////        }else {
////            Glide.with(context).load(selStudent.getHeadImgId()).into(iv_headImg);
////        }
////
////        //设置数据
////        et_name.setText(selStudent.getName());
////        et_age.setText(selStudent.getAge()+"");
////        //移动光标到末尾
////        SomeUtils.moveFocus(et_name);
////
////        builder.setView(view);
////        builder.setCancelable(true); //点返回是否隐藏
////        builder.setTitle("输入修改（暂修改姓名、年龄）");
////
////        builder.setNegativeButton("取消",null)
////                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
////                    @Override
////                    public void onClick(DialogInterface dialogInterface, int i) {
////                        //修改选中item的内容
////                        selStudent.setName(et_name.getText().toString());
////                        selStudent.setAge(Integer.parseInt(et_age.getText().toString()));
////                        //刷新列表
////                        adapter.notifyItemChanged(position);
////                    }
////                });
////
////        AlertDialog dialog = builder.create();
////        dialog.setCanceledOnTouchOutside(true);//点dialog的外面隐藏
////
////        dialog.show();
////
////    }
//
//    /**
//     * 执行查找操作
//     * 这个查找操作和其他的增加、删除操作可能还有bug
//     * 本来查询操作要配合数据库操作才是，这里就不做那么麻烦去修改查询的bug
//     * @param keywoed 查找关键字，目前只能查找一个关键字
//     */
////    private void doQuery(String keywoed)
////    {
////        //如果查找关键字为空或者null就返回全部的数据
////        if(keywoed==null || keywoed.isEmpty())
////        {
////            //临时集合有数据才会去重新查找
////            if(tempList!=null && tempList.size()>0)
////            {
////                list.clear();
////                list.addAll(tempList);
////                //刷新列表
////                adapter.notifyDataSetChanged();
////                //查找完全部数据后清空临时数据集合
////                tempList.clear();
////            }else {
////                return;
////            }
////
////        }else {
////            //临时集合里面没数据才会去保存数据
////            if(tempList.size()==0)
////            {
////                //临时保存全部数据
////                tempList.addAll(list);
////                //模糊查找数据返回数据
////                searchList = SomeUtils.fuzzyQuery(keywoed,list);
////                list.clear();
////                list.addAll(searchList);
////            }
////            //刷新列表
////            adapter.notifyDataSetChanged();
////        }
////
////    }
//
//
//}

