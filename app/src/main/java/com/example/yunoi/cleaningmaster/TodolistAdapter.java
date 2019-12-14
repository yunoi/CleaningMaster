package com.example.yunoi.cleaningmaster;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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
    public static int score=0;
    private SQLiteDatabase db;
    private alarmClickListener listener = null;
    public static int exp=0;

    //생성자
    public TodolistAdapter(int layout, ArrayList<TodolistVo> list) {
        this.layout = layout;
        this.list = list;
    }

    // 클릭 리스너 인터페이스
    public interface alarmClickListener {
        void onAlarmClick(View v, int position);
        void onSwitchClick(CompoundButton buttonView, boolean isChecked);
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
                        String text=list.get(position).getTodolist_text();
                        list.remove(position);
                        notifyDataSetChanged();
                        deleteCleningArea(text,v.getContext()); //DB 삭제부분
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
        customViewHolder.todolist_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(listener != null){
                    listener.onSwitchClick(buttonView, isChecked);
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
        private SwipeLayout swipe_sample1;
        private LinearLayout todo_linearLayout;
        private CheckBox todolist_checkBox;
        private Switch todolist_switch;
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


            //체크박스  true / false
            todolist_checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                    if (isChecked==true){
                        strikeThroughPainting.color(Color.rgb(2, 72, 112))
                                .strokeWidth(4).totalTime(10_0L).strikeThrough();
                        checkcount++;//체크 카운트
                        score+=100;

                        //스낵바 설정(체크할 시 경험치 확인 스낵바)
                        Snackbar snackbar=Snackbar.make(todo_linearLayout,"경험치가 적립되었습니다! 조금만 더 힘내요!\n경험치 :"+score,Snackbar.LENGTH_SHORT);
                        snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                        Snackbar.SnackbarLayout layout=(Snackbar.SnackbarLayout) snackbar.getView();
                        layout.setPadding(10,10,50,10);
                        View snackbarView=snackbar.getView();
                        TextView tv=(TextView)snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                        tv.setTextColor(Color.WHITE);
                        tv.setTextSize(16);
                        snackbarView.setBackgroundColor(Color.parseColor("#024873"));



                        //체크한 카운트가 만든 리스트와 맞을때
                        if (checkcount==list.size()){

                            score+=200;
                            //스낵바 설정(체크할 시 경험치 확인 스낵바)
                                tv.setText("축하합니다! 모든 목표를 다 달성하셨습니다!\n보너스 경험치(+200exp)가 적립되었습니다!\n경험치 :"+ score);
                                snackbar.setDuration(Snackbar.LENGTH_INDEFINITE);
                                tv.setPadding(60,60,60,60);
                                tv.setTextSize(16);
                                tv.setMaxLines(3);

                                snackbar.setAction("확인", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent=new Intent(itemView.getContext(),ExpShowActivity.class);
                                        startActivity(itemView.getContext(),intent,null);
                                    }
                                });


                        }

                        snackbar.show();

                    }else {
                        checkcount--;
                        strikeThroughPainting.clearStrikeThrough();
                    }


                }
            });

        }   // end of constructor

    } // end of customViewHolder class

    // 청소리스트 DB 삭제 부분
    public void deleteCleningArea(String text, Context context){
        db = DBHelper.getInstance(context.getApplicationContext()).getWritableDatabase();
        db.execSQL("DELETE FROM cleaningTBL WHERE task='"+text+"';");
        Log.d(TAG,"DB 삭제됨");
    }

}
