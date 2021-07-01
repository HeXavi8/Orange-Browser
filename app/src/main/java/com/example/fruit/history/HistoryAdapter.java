package com.example.fruit.history;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fruit.R;
import com.example.fruit.bean.History;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private List<History> mHistoryItems;
    private HistoryFragment mHistoryFragment;
    private boolean ifShowCheckBox;


    static class ViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        TextView title;
        TextView url;
        CheckBox checkBox;
        TextView time;

        public ViewHolder(@NonNull @org.jetbrains.annotations.NotNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            title = itemView.findViewById(R.id.tv_title);
            url = itemView.findViewById(R.id.tv_url);
            time = itemView.findViewById(R.id.tv_time);
            checkBox = itemView.findViewById(R.id.checkbox);
            checkBox.setClickable(false);
        }
    }

    public HistoryAdapter(List<History> historyItems, HistoryFragment historyFragment) {
        mHistoryItems = historyItems;
        mHistoryFragment = historyFragment;
        ifShowCheckBox = false;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item,
                parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull HistoryAdapter.ViewHolder holder, int position) {
        History historyItem = mHistoryItems.get(position);
        holder.title.setText(historyItem.getTitle());
        holder.url.setText(historyItem.getUrl());
        holder.time.setText(historyItem.getTime());
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
                    mHistoryFragment.onClick(view, position);
                } else {
                    mHistoryFragment.goToSearch(holder.url.getText().toString());

                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ifShowCheckBox = true;
                return mHistoryFragment.onLongClick(view, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mHistoryItems.size();
    }

    public void setShowCheckBox(Boolean val) {
        ifShowCheckBox = val;
    }
}
