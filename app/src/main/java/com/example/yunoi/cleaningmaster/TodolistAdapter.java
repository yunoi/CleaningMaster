package com.example.yunoi.cleaningmaster;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;

import java.util.ArrayList;

import lib.mozidev.me.extextview.ExTextView;
import lib.mozidev.me.extextview.StrikeThroughPainting;

import static android.support.v4.content.ContextCompat.startActivity;

public class TodolistAdapter extends RecyclerView.Adapter<TodolistAdapter.CustomViewHolder> {

    private int layout;
    private ArrayList<TodolistVo> list;
    private int checkcount=0;
    private static final String TAG="확인";
    private alarmClickListener listener = null;


    //생성자
    public TodolistAdapter(int layout, ArrayList<TodolistVo> list) {
        this.layout = layout;
        this.list = list;
    }

    // 클릭 리스너 인터페이스
    public interface alarmClickListener {
        void onAlarmClick(View v, int position);
        void onSwitchClick(View v, int position);
    }

    public void setAlarmClickListener(alarmClickListener listener){
        this.listener = listener;
    }

    public void setSwitchClickListener(alarmClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public TodolistAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(layout, viewGroup, false);
        CustomViewHolder customViewHolder = new CustomViewHolder(view);


        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final TodolistAdapter.CustomViewHolder customViewHolder, final int position) {

        customViewHolder.todolist_text.setText(list.get(position).getTodolist_text());
        final int taskCount=list.get(position).getTaskcount();

        //취소 버튼 액션
        customViewHolder.swipe_sample1.setShowMode(SwipeLayout.ShowMode.LayDown);
        customViewHolder.swipe_sample1.findViewById(R.id.delete_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                //취소 알런트
                final View alertDialogView=View.inflate(v.getContext(),R.layout.dialog_delete_layout,null);
                AlertDialog.Builder builder=new AlertDialog.Builder(v.getContext(),R.style.MyDialogTheme);
                builder.setView(alertDialogView);
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        list.remove(position);
                        notifyDataSetChanged();
                        Snackbar snackbar=Snackbar.make(customViewHolder.todo_linearLayout,"삭제되었습니다!",Snackbar.LENGTH_SHORT);

                        snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                        View snackbarView=snackbar.getView();
                        TextView tv=(TextView)snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                        tv.setTextColor(Color.WHITE);

                        snackbarView.setBackgroundColor(Color.parseColor("#024873"));

                        snackbar.show();

                    }
                });
                builder.setNegativeButton("취소",null);
                builder.show();
            }
        });

        // 알람 설정 버튼 액션
        customViewHolder.todolist_alram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null){
                    listener.onAlarmClick(v, position);
                }
            }
        });

        // 반복알림 스위치 설정 버튼 액션
        customViewHolder.todolist_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null){
                    listener.onSwitchClick(v, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        private ImageButton todolist_alram;
        private ExTextView todolist_text;
        private Switch todolist_switch;
        private SwipeLayout swipe_sample1;
        private LinearLayout todo_linearLayout;
        private CheckBox todolist_checkBox;
        StrikeThroughPainting strikeThroughPainting;

        public CustomViewHolder(@NonNull final View itemView) {
            super(itemView);

            todolist_alram=itemView.findViewById(R.id.todolist_alram);
            todolist_text=itemView.findViewById(R.id.todolist_text);
            todolist_switch=itemView.findViewById(R.id.todolist_switch);
            swipe_sample1=itemView.findViewById(R.id.swipe_sample1);
            todo_linearLayout=itemView.findViewById(R.id.todo_linearLayout);
            todolist_checkBox=itemView.findViewById(R.id.todolist_checkBox);
            strikeThroughPainting = new StrikeThroughPainting(todolist_text);


            todolist_checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                    if (isChecked==true){
                        strikeThroughPainting.color(Color.rgb(2, 72, 112))
                                .strokeWidth(4).totalTime(10_0L).strikeThrough();
                        checkcount++;//체크 카운트

                        //체크한 카운트가 만든 리스트와 맞을때
                        if (checkcount==TodolistFragment.taskcount){

                            Intent intent=new Intent(itemView.getContext(),ExpShowActivity.class);
                            startActivity(itemView.getContext(),intent,null);

                        }


                    }else {
                        checkcount--;
                        strikeThroughPainting.clearStrikeThrough();
                    }

                    Log.d(TAG,"체크 카운트 확인 : "+checkcount+" / 총 리스트 개수 : "+String.valueOf(TodolistFragment.taskcount));

                }
            });

        }   // end of constructor



    } // end of customViewHolder class
}
