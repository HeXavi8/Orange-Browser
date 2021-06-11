package com.example.fruit.history;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

//import com.bumptech.glide.Glide;
import com.example.fruit.R;
import com.example.fruit.history.HistoryFragment;

import java.time.Instant;
import java.util.ArrayList;

//public class HistoryRecyclerviewAdapter {
//}

public class HistoryRecyclerviewAdapter extends RecyclerView.Adapter<HistoryRecyclerviewAdapter.historyViewHolder> implements View.OnClickListener,View.OnLongClickListener {



    private Context mContext;
    private ArrayList<HistoryItem> myList;
    private int itemLayoutId;

    private historyViewHolder holder;
    private HistoryItem historyItem;
    //private Instant Glide;

    public HistoryRecyclerviewAdapter(Context context, ArrayList<HistoryItem> arrayList,int itemLayoutId) {

        this.mContext = context;
        this.myList = arrayList;
        this.itemLayoutId = itemLayoutId;//布局，0-LinearLayoutManager，1-GridLayoutManager,2-
    }



    @Override

    public int getItemCount() {

        return myList.size();

    }

    @Override

    public historyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        Log.i("main","onCreateViewHolder---myList.size()=="+myList.size());
        View view = LayoutInflater.from(mContext).inflate(R.layout.history_item, null, true);//R.layout.layout_historyItem_grid_item

        holder = new historyViewHolder(view);

        //给每条布局设置点击事件  也可以用其他方式
        view.setOnClickListener(this);
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        //长按事件监听
        view.setOnLongClickListener(this);

        return holder;

    }



    @Override

    public void onBindViewHolder(historyViewHolder holder, final int position) {
//        Log.i("main","onBindViewHolder---myList====="+myList.size()+"-------position=="+position);

        historyItem = myList.get(position);

        holder.tv_title.setText("名字："+historyItem.getTitle());
        holder.tv_url.setText("URL："+historyItem.getUrl()+"");

        holder.itemView.setTag(position);

        //根据选中与否来设置checkbox
        holder.checkbox.setChecked(historyItem.isSelected());

        //整个item的点击事件在 adapter.setItemClickListener监听，这里也对checkbox单独设置监听
        holder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myList.get(position).setSelected(!historyItem.isSelected());
            }
        });

        //统计选中的个数，返回给设置监听的地方
        if(mCountCheckBoxListener!=null)
        {
            int checkNum = 0;
            for(HistoryItem stu:myList)
            {
                if(stu.isSelected())
                {
                    checkNum++;
                }
            }
            //这里得出的结果会返回到设置监听的地方
            mCountCheckBoxListener.countNumber(checkNum,myList.size());
        }

    }

    /**
     * 设置ViewHolder
     */
    public class historyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_title;
        private TextView tv_url;
        private CheckBox checkbox;

        public historyViewHolder(View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_url = itemView.findViewById(R.id.tv_url);

            checkbox = itemView.findViewById(R.id.checkbox);
        }

    }

    //点击事件监听
    public OnItemClickListener mItemClickListener;

    @Override

    public void onClick(View v) {
        //设置item的点击事件
        if (mItemClickListener != null)
        {
            mItemClickListener.onItemClick((Integer) v.getTag());
        }

    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    //长按事件监听
    public  OnItemLongClickListener mItemLongClickListener;
    @Override
    public boolean onLongClick(View view) {
        if(mItemLongClickListener!=null)
        {
            mItemLongClickListener.onItemLongClick((Integer) view.getTag());
        }
        return false;
    }
    public interface OnItemLongClickListener {
        void onItemLongClick(int position);
    }

    public void setItemLongClickListener(OnItemLongClickListener itemLongClickListener) {
        mItemLongClickListener = itemLongClickListener;
    }


    //统计勾选个数监听
    public CountCheckBoxListener mCountCheckBoxListener;
    public interface CountCheckBoxListener {
        void countNumber(int checkNum,int total);
    }

    public void setCountCheckBoxListener(CountCheckBoxListener countCheckBoxListener)
    {
        mCountCheckBoxListener = countCheckBoxListener;
    }

}