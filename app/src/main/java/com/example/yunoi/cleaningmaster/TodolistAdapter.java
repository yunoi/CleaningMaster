package com.example.yunoi.cleaningmaster;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import lib.mozidev.me.extextview.ExTextView;
import lib.mozidev.me.extextview.StrikeThroughPainting;

import static android.support.v4.content.ContextCompat.startActivity;

public class TodolistAdapter extends RecyclerView.Adapter<TodolistAdapter.CustomViewHolder> {

    private int layout;
    private ArrayList<TodolistVo> list;
    private int checkcount=0;
    private static final String TAG="확인";
    private SQLiteDatabase db;
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
    public TodolistAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(layout, viewGroup, false);
        CustomViewHolder customViewHolder = new CustomViewHolder(view);

        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final TodolistAdapter.CustomViewHolder customViewHolder, final int position) {
        final Context context=customViewHolder.itemView.getContext();
        final StrikeThroughPainting strikeThroughPainting=new StrikeThroughPainting(customViewHolder.todolist_text);
        final String taskText=list.get(position).getTodolist_text();
        customViewHolder.todolist_text.setText(taskText);
        final int allListSize=selectAllListSize(context); //전체 리스트 사이즈 (구역 상관없는 전체 리스트사이즈)

        //체크 유무를 가져옴.!!!
        final int check=list.get(position).getCheckcount();
        if (check==1){
            //true
            strikeThroughPainting.color(Color.rgb(2, 72, 112))
                    .strokeWidth(4).totalTime(10_0L).strikeThrough();
            customViewHolder.todolist_checkBox.setChecked(true);
        }else if (check==0){
            //false
            customViewHolder.todolist_checkBox.setChecked(false);
        }




        //체크박스  true / false 클릭 리스너
        customViewHolder.todolist_checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                //현재 년,월,일
                Calendar calendar=Calendar.getInstance();
                Date date=calendar.getTime();
                String year=new SimpleDateFormat("YYYY").format(date);
                String month=new SimpleDateFormat("MM").format(date);
                String day=new SimpleDateFormat("dd").format(date);

                final int currentYear=Integer.parseInt(year);
                final int currentMonth=Integer.parseInt(month);
                final int currentDay=Integer.parseInt(day);

                Log.d(TAG,"날짜 : "+currentYear+"년"+currentMonth+"월"+currentDay+"일");


                if (check==1) {
                    //누른 값이 체크가 되어있으면 리턴

                    if (isChecked==false){
                        Log.d(TAG,"눌렀던것 다시 false");
//                        insertCheckFalse(customViewHolder.itemView.getContext(),0,taskText,TodolistFragment.groupText);
                        strikeThroughPainting.clearStrikeThrough();
                    }
                    if (isChecked==true){
                        Log.d(TAG,"눌렀던것 다시 true exp는 안들어감.");
                        strikeThroughPainting.color(Color.rgb(2, 72, 112))
                                .strokeWidth(4).totalTime(10_0L).strikeThrough();

                        Snackbar snackbar=Snackbar.make(TodolistFragment.todo_constraintLayout,"이미 완료했습니다!",Snackbar.LENGTH_SHORT);
                        snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                        Snackbar.SnackbarLayout layout=(Snackbar.SnackbarLayout) snackbar.getView();
                        layout.setPadding(10,10,50,10);
                        View snackbarView=snackbar.getView();
                        TextView tv=(TextView)snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                        tv.setTextColor(Color.WHITE);
                        tv.setTextSize(16);
                        tv.setMaxLines(3);
                        tv.setTextSize(16);
                        tv.setPadding(60,60,60,60);
                        snackbarView.setBackgroundColor(Color.parseColor("#024873"));
                        snackbar.show();

                    }

                    return;

                }

                if (check==0){
                        //누른 값이 체크가 안되어있으면 체크 업데이트!
                    if (isChecked){

                        checkBoxTrue(strikeThroughPainting,context,taskText);
                        final int checkSize=selectCheckBoxCount(context,currentYear,currentMonth,currentDay);
                        Log.d(TAG,"실시간 체크사이즈 : "+checkSize);
                        Log.d(TAG,"실시간 리스트 사이즈 : "+allListSize);

                        //체크한 카운트가 만든 리스트와 맞을때
                        if (checkSize==allListSize){

                            TodolistFragment.score+=200;

                            Snackbar snackbar=Snackbar.make(TodolistFragment.todo_constraintLayout,"축하합니다! 모든 목표를 다 달성하셨습니다!\n보너스 경험치(+200exp)가 적립되었습니다!\n경험치 :"+  TodolistFragment.score,Snackbar.LENGTH_INDEFINITE);
                            snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                            Snackbar.SnackbarLayout layout=(Snackbar.SnackbarLayout) snackbar.getView();
                            layout.setPadding(10,10,50,10);
                            View snackbarView=snackbar.getView();
                            TextView tv=(TextView)snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                            tv.setTextColor(Color.WHITE);
                            tv.setTextSize(16);
                            tv.setMaxLines(3);
                            tv.setTextSize(16);
                            tv.setPadding(60,60,60,60);
                            snackbarView.setBackgroundColor(Color.parseColor("#024873"));


                            snackbar.setAction("확인", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent=new Intent(context,ExpShowActivity.class);
                                    startActivity(context,intent,null);
                                    insertScore(TodolistFragment.score,v.getContext());
                                }
                            });

                            snackbar.show();

                        }

                    }else {
                        strikeThroughPainting.clearStrikeThrough();
                    }
                }




            }
        });




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


        public CustomViewHolder(@NonNull final View itemView) {
            super(itemView);

            todolist_alram=itemView.findViewById(R.id.todolist_alram);
            todolist_text=itemView.findViewById(R.id.todolist_text);
            todolist_switch=itemView.findViewById(R.id.todolist_switch);
            swipe_sample1=itemView.findViewById(R.id.swipe_sample1);
            todo_linearLayout=itemView.findViewById(R.id.todo_linearLayout);
            todolist_checkBox=itemView.findViewById(R.id.todolist_checkBox);



        }   // end of constructor

    } // end of customViewHolder class

    //checkBox True 인경우 함수화
    public void checkBoxTrue(StrikeThroughPainting strikeThroughPainting, final Context context, String taskText){

        //현재 년,월,일
        Calendar calendar=Calendar.getInstance();
        Date date=calendar.getTime();
        String year=new SimpleDateFormat("YYYY").format(date);
        String month=new SimpleDateFormat("MM").format(date);
        String day=new SimpleDateFormat("dd").format(date);

        final int currentYear=Integer.parseInt(year);
        final int currentMonth=Integer.parseInt(month);
        final int currentDay=Integer.parseInt(day);

        Log.d(TAG,"날짜 : "+currentYear+"년"+currentMonth+"월"+currentDay+"일");

        strikeThroughPainting.color(Color.rgb(2, 72, 112))
                .strokeWidth(4).totalTime(10_0L).strikeThrough();
        TodolistFragment.score+=100;
        //스낵바 설정(체크할 시 경험치 확인 스낵바)
        Snackbar snackbar=Snackbar.make(TodolistFragment.todo_constraintLayout,"경험치가 적립되었습니다! 조금만 더 힘내요!\n경험치 :"+TodolistFragment.score,Snackbar.LENGTH_SHORT);
        snackbar.setActionTextColor(Color.parseColor("#ffffff"));
        Snackbar.SnackbarLayout layout=(Snackbar.SnackbarLayout) snackbar.getView();
        layout.setPadding(10,10,50,10);
        View snackbarView=snackbar.getView();
        TextView tv=(TextView)snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        tv.setTextSize(16);
        snackbarView.setBackgroundColor(Color.parseColor("#024873"));

        //체크 True DB저장 , 스코어 저장

        insertScore(TodolistFragment.score,context);
        insertCheck(context,currentYear,currentMonth,currentDay,1,taskText,TodolistFragment.groupText);

        snackbar.show();
    }

    //DB 삭제 부분
    public void deleteCleningArea(String text, Context context){
        db = DBHelper.getInstance(context).getWritableDatabase();
        db.execSQL("DELETE FROM cleaningTBL WHERE task='"+text+"';");
        Log.d(TAG,"DB 삭제됨");
    }


    //DB 스코어 저장하기
    public void insertScore(int score,Context context){
        db = DBHelper.getInstance(context).getWritableDatabase();

        Cursor cursor;
        cursor=db.rawQuery("SELECT * FROM profileTBL",null);
        String nickname=null;
        while (cursor.moveToNext()){

            nickname=cursor.getString(0);
        }
        Log.d(TAG,"닉네임 확인 : "+nickname);


        db.execSQL("UPDATE profileTBL SET score="+score+" WHERE NickName="+"'"+nickname+"'"+";");

        cursor.close();

        Log.d(TAG,"scoer DB 저장 : "+score);

    }
    // 체크 정보 업데이트 (0_false, 1_true)
    public void insertCheck(Context context,int currentYear,int currentMonth,int currentDay,int checkCount,String task,String groupName){
        db = DBHelper.getInstance(context).getWritableDatabase();
        Log.d(TAG,"가져온 task : "+task);
        db.execSQL("UPDATE cleaningTBL SET year="+currentYear+", month ="+currentMonth+", day="+currentDay+", checkCount="+checkCount+" WHERE task ='"+task+"' AND area ='"+groupName+"' ;");
        Toast.makeText(context,"업데이트 저장되었습니다.",Toast.LENGTH_SHORT).show();


        Log.d(TAG,"저장된 시간 : "+currentYear+currentMonth+currentDay);
        Log.d(TAG,"저장된 체크 : "+checkCount);
    }


    //체크 한 개수 가져오기
    public int selectCheckBoxCount(Context context,int year,int month,int day){
        db = DBHelper.getInstance(context.getApplicationContext()).getWritableDatabase();
        Cursor cursor;
        cursor = db.rawQuery("SELECT checkCount FROM cleaningTBL WHERE checkCount="+1+" AND year="+year+" AND month="+month+" AND day="+day+";", null);
        int size=cursor.getCount();

        cursor.close();
        return size;
    }

    //전체 저장 되어있는 리스트 사이즈
    public int selectAllListSize(Context context){
        db = DBHelper.getInstance(context.getApplicationContext()).getWritableDatabase();
        Cursor cursor;
        cursor = db.rawQuery("SELECT * FROM cleaningTBL ;", null);
        int allSize=cursor.getCount();
        cursor.close();
        return allSize;
    }



}
