package com.example.yunoi.cleaningmaster;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import java.util.ArrayList;

import lib.mozidev.me.extextview.ExTextView;


public class TodayListAdapter extends RecyclerView.Adapter<TodayListAdapter.CustomViewHolder> {


    int layout;
    ArrayList<TodayListVO> list;

    public TodayListAdapter(int layout, ArrayList<TodayListVO> list) {
        this.layout = layout;
        this.list = list;
    }

    @NonNull
    @Override
    public TodayListAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(layout, viewGroup, false);
        TodayListAdapter.CustomViewHolder customViewHolder=new CustomViewHolder(view);

        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TodayListAdapter.CustomViewHolder customViewHolder, int position) {

        customViewHolder.todaylist_text.setText(list.get(position).getTask());




    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        CheckBox todaylist_checkBox;
        ExTextView todaylist_text;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            todaylist_checkBox=itemView.findViewById(R.id.todaylist_checkBox);
            todaylist_text=itemView.findViewById(R.id.todaylist_text);

        }
    }
}
