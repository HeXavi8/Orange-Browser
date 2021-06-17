package com.example.fruit.history;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fruit.MyAppliaction;
import com.example.fruit.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private List<HistoryItem> mHistoryItems;


    static class ViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        TextView name;
        TextView url;

        public ViewHolder(@NonNull @org.jetbrains.annotations.NotNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            name = itemView.findViewById(R.id.tv_title);
            url = itemView.findViewById(R.id.tv_url);
        }
    }

    public HistoryAdapter(List<HistoryItem> historyItems) {
        mHistoryItems = historyItems;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item,
                parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("click");
                Toast.makeText(MyAppliaction.getContext(), "click", Toast.LENGTH_SHORT).show();
            }
        });
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                System.out.println("long");
                Toast.makeText(MyAppliaction.getContext(), "long", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull HistoryAdapter.ViewHolder holder, int position) {
        HistoryItem historyItem = mHistoryItems.get(position);
        holder.name.setText(historyItem.getTitle());
        holder.url.setText(historyItem.getUrl());
    }

    @Override
    public int getItemCount() {
        return mHistoryItems.size();
    }
}
