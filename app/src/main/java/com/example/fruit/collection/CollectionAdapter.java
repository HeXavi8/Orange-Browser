package com.example.fruit.collection;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fruit.R;
import com.example.fruit.bean.Collection;

import com.example.fruit.collection.CollectionAdapter;
import com.example.fruit.collection.CollectionFragment;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.ViewHolder>{
    private List<Collection> mCollectionItems;
    private CollectionFragment mCollectionFragment;
    private boolean ifShowCheckBox;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        TextView title;
        TextView url;
        CheckBox checkBox;

        public ViewHolder(@NonNull @org.jetbrains.annotations.NotNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            title = itemView.findViewById(R.id.tv_title);
            url = itemView.findViewById(R.id.tv_url);
            checkBox = itemView.findViewById(R.id.checkbox);
            checkBox.setClickable(false);
        }
    }

    public CollectionAdapter(List<Collection> collectionItems, CollectionFragment collectionFragment) {
        mCollectionItems = collectionItems;
        mCollectionFragment = collectionFragment;
        ifShowCheckBox = false;
    }


    @NonNull
    @NotNull
    @Override
    //与collection_item.xml绑定
    public CollectionAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.collection_item,
                parent, false);
        CollectionAdapter.ViewHolder viewHolder = new CollectionAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull @NotNull CollectionAdapter.ViewHolder holder, int position) {
        Collection collectionItem = mCollectionItems.get(position);
        holder.title.setText(collectionItem.getTitle());
        holder.url.setText(collectionItem.getUrl());
        //holder.time.setText(collectionItem.getTime());
        if (ifShowCheckBox) {
            holder.checkBox.setVisibility(View.VISIBLE);
        } else {
            holder.checkBox.setVisibility(View.INVISIBLE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ifShowCheckBox) {
                    if (holder.checkBox.isChecked()) {
                        holder.checkBox.setChecked(false);
                    } else {
                        holder.checkBox.setChecked(true);
                    }
                    mCollectionFragment.onClick(view, position);
                } else {
                    mCollectionFragment.goToSearch(holder.url.getText().toString());
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ifShowCheckBox = true;
                return mCollectionFragment.onLongClick(view, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCollectionItems.size();
    }

    public void setShowCheckBox(Boolean val) {
        ifShowCheckBox = val;
    }


}
