package com.example.yunoi.cleaningmaster;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;

import java.util.ArrayList;

public class TodolistAdapter extends RecyclerView.Adapter<TodolistAdapter.CustomViewHolder> {

    int layout;
    ArrayList<TodolistVo> list;

    //생성자
    public TodolistAdapter(int layout, ArrayList<TodolistVo> list) {
        this.layout = layout;
        this.list = list;
    }

    @NonNull
    @Override
    public TodolistAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view=LayoutInflater.from(viewGroup.getContext()).inflate(layout,viewGroup,false);
        CustomViewHolder customViewHolder=new CustomViewHolder(view);


        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TodolistAdapter.CustomViewHolder customViewHolder, int position) {

        customViewHolder.todolist_text.setText(list.get(position).getTodolist_text());
        customViewHolder.swipe_sample1.setShowMode(SwipeLayout.ShowMode.LayDown);
        customViewHolder.swipe_sample1.getSurfaceView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return list !=null ? list.size() : 0 ;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        private ImageButton todolist_alram;
        private TextView todolist_text;
        private Switch todolist_switch;
        private SwipeLayout swipe_sample1;


        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);


            todolist_alram=itemView.findViewById(R.id.todolist_alram);
            todolist_text=itemView.findViewById(R.id.todolist_text);
            todolist_switch=itemView.findViewById(R.id.todolist_switch);
            swipe_sample1=itemView.findViewById(R.id.swipe_sample1);

        }
    }
}
