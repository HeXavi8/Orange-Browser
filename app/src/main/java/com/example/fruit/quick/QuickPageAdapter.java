package com.example.fruit.quick;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fruit.R;

import com.example.fruit.bean.QuickPage;

import java.util.ArrayList;


public class QuickPageAdapter extends RecyclerView.Adapter<QuickPageAdapter.QuickViewHolder> implements View.OnClickListener,View.OnLongClickListener{


    private Context mContext;
    private ArrayList<QuickPage> mQuickList;
    //private int itemLayoutId;

    private QuickViewHolder mQuickViewHolder;
    private QuickPage mQuickPage;

    public QuickPageAdapter(Context context, ArrayList<QuickPage> quickList) {
        this.mContext = context;
        this.mQuickList = quickList;
    }



    @Override

    public int getItemCount() {

        return mQuickList.size();

    }

    @Override

    public QuickViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        Log.i("main","onCreateViewHolder---mQuickList.size()=="+mQuickList.size());
        View view = LayoutInflater.from(mContext).inflate(R.layout.quick_page_item, null, true);//R.layout.layout_QuickPages_grid_item

        mQuickViewHolder = new QuickViewHolder(view);

        //给每个quick page 设置点击事件
        view.setOnClickListener(this);

        //长按事件监听
        view.setOnLongClickListener(this);

        return mQuickViewHolder;

    }



    @Override

    public void onBindViewHolder(QuickViewHolder mQuickViewHolder, final int position) {
//        Log.i("main","onBindViewHolder---mQuickList====="+mQuickList.size()+"-------position=="+position);

        mQuickPage = mQuickList.get(position);

        mQuickViewHolder.tv_title.setText(mQuickPage.getTitle());

        //mQuickViewHolder.tv_url.setText("URL："+mQuickPage.getUrl()+"");

        //设置图片
        Glide.with(mContext).load(mQuickPage.getImgPath()).into(mQuickViewHolder.iv_headImg);

        mQuickViewHolder.itemView.setTag(position);

    }

    /**
     * 设置ViewHolder
     */
    public class QuickViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_headImg;
        private TextView tv_title;
        //private TextView tv_url;

        public QuickViewHolder(View itemView) {
            super(itemView);

            iv_headImg = (ImageView)itemView.findViewById(R.id.iv_headImg);
            tv_title = (TextView)itemView.findViewById(R.id.tv_title);
            //tv_url = (TextView)itemView.findViewById(R.id.tv_url);

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
    public OnItemLongClickListener mItemLongClickListener;
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


}
