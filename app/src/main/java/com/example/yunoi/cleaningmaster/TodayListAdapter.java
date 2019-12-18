package com.example.yunoi.cleaningmaster;

import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import lib.mozidev.me.extextview.ExTextView;
import lib.mozidev.me.extextview.StrikeThroughPainting;

import static android.support.v4.content.ContextCompat.startActivity;


public class TodayListAdapter extends RecyclerView.Adapter<TodayListAdapter.CustomViewHolder> {


    int layout;
    ArrayList<TodayListVO> list;
    private SQLiteDatabase db;
    private static String TAG="확인";
    private int checkSize;
    private String area;

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
    public void onBindViewHolder(@NonNull final TodayListAdapter.CustomViewHolder customViewHolder, int position) {

        final Context context=customViewHolder.itemView.getContext();
        final StrikeThroughPainting strikeThroughPainting = new StrikeThroughPainting(customViewHolder.todaylist_text);
        final String taskText=list.get(position).getTask();
        area=list.get(position).getArea();
        customViewHolder.todaylist_text.setText(list.get(position).getTask());
        customViewHolder.todaylist_area.setText(area);
        final int isCheckClear = selectIsCheckClear(context);

        //체크 유무를 가져옴.!!!
        final int check = list.get(position).getCheckCount();
        if (check == 1) {
            //true
            strikeThroughPainting.color(Color.rgb(2, 72, 112))
                    .strokeWidth(4).totalTime(10_0L).strikeThrough();
            customViewHolder.todaylist_checkBox.setChecked(true);
        } else if (check == 0) {
            //false
            customViewHolder.todaylist_checkBox.setChecked(false);
        }


        //체크박스  true / false 클릭 리스너
        customViewHolder.todaylist_checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //현재 년,월,일
                Calendar calendar = Calendar.getInstance();
                Date date = calendar.getTime();
                String year = new SimpleDateFormat("yyyy").format(date);
                String month = new SimpleDateFormat("MM").format(date);
                String day = new SimpleDateFormat("dd").format(date);

                final int currentYear = Integer.parseInt(year);
                final int currentMonth = Integer.parseInt(month);
                final int currentDay = Integer.parseInt(day);

                Log.d(TAG, "날짜 : " + currentYear + "년" + currentMonth + "월" + currentDay + "일");


                if (check == 1) {
                    //누른 값이 체크가 되어있으면 리턴

                    if (isChecked == false) {
                        Log.d(TAG, "눌렀던것 다시 false");
                        strikeThroughPainting.clearStrikeThrough();
                    }
                    if (isChecked == true) {
                        Log.d(TAG, "눌렀던것 다시 true exp는 안들어감.");
                        strikeThroughPainting.color(Color.rgb(2, 72, 112))
                                .strokeWidth(4).totalTime(10_0L).strikeThrough();

                        Snackbar snackbar = Snackbar.make(TodayListFragment.todaylist_LinearLayout, "이미완료했습니다!", Snackbar.LENGTH_SHORT);
                        snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
                        layout.setPadding(20, 5, 5, 5);
                        View snackbarView = snackbar.getView();
                        TextView tv = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                        tv.setTextColor(Color.WHITE);
                        tv.setMaxLines(3);
                        tv.setTextSize(16);
                        snackbarView.setBackgroundColor(Color.parseColor("#024873"));
                        snackbar.show();

                    }

                }
                Log.d("확인","check  값 : "+check);
                if (check == 0) {
                    //누른 값이 체크가 안되어있으면 체크 업데이트!
                    final int ischeckCount=selectIsCheck(context,area,taskText);
                    Log.d("확인","ischeckCount  값 : "+ischeckCount);
                    if (isChecked) {

                            if (isCheckClear==1 && ischeckCount==1){
                                    if (check == 1) {
                                        //true
                                        strikeThroughPainting.color(Color.rgb(2, 72, 112))
                                                .strokeWidth(4).totalTime(10_0L).strikeThrough();
                                        customViewHolder.todaylist_checkBox.setChecked(true);
                                    } else if (check == 0) {
                                        //false
                                        customViewHolder.todaylist_checkBox.setChecked(false);
                                    }
                                    if (isChecked){
                                        customViewHolder.todaylist_checkBox.setChecked(true);
                                        Log.d(TAG, "눌렀던것 다시 true exp는 안들어감.");
                                        strikeThroughPainting.color(Color.rgb(2, 72, 112))
                                                .strokeWidth(4).totalTime(10_0L).strikeThrough();

                                        Snackbar snackbar = Snackbar.make(TodayListFragment.todaylist_LinearLayout, "이미완료했습니다!", Snackbar.LENGTH_SHORT);
                                        snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                                        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
                                        layout.setPadding(20, 5, 5, 5);
                                        View snackbarView = snackbar.getView();
                                        TextView tv = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                                        tv.setTextColor(Color.WHITE);
                                        tv.setTextSize(16);
                                        snackbarView.setBackgroundColor(Color.parseColor("#024873"));
                                        snackbar.show();

                                    }else {
                                        strikeThroughPainting.clearStrikeThrough();
                                        customViewHolder.todaylist_checkBox.setChecked(false);
                                    }


                                return;

                            }

                            if (ischeckCount==1){
                                customViewHolder.todaylist_checkBox.setChecked(true);
                                Log.d(TAG, "눌렀던것 다시 true exp는 안들어감.");
                                strikeThroughPainting.color(Color.rgb(2, 72, 112))
                                        .strokeWidth(4).totalTime(10_0L).strikeThrough();

                                Snackbar snackbar = Snackbar.make(TodayListFragment.todaylist_LinearLayout, "이미완료했습니다!", Snackbar.LENGTH_SHORT);
                                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                                Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
                                layout.setPadding(20, 5, 5, 5);
                                View snackbarView = snackbar.getView();
                                TextView tv = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                                tv.setTextColor(Color.WHITE);
                                tv.setTextSize(16);
                                snackbarView.setBackgroundColor(Color.parseColor("#024873"));
                                snackbar.show();
                                return;
                            }
                             checkBoxTrue(strikeThroughPainting, context, taskText);
                            checkSize = selectCheckBoxCount(context, currentYear, currentMonth, currentDay);
                            Log.d(TAG, "실시간 체크사이즈 : " + checkSize);
                            Log.d(TAG, "실시간 리스트 사이즈 : " + list.size());
                            //스낵바 설정(체크할 시 경험치 확인 스낵바)
                            stnackBar("경험치가 적립되었습니다!(+100xp)\n경험치 :");

                            Intent intent = new Intent(context, ExpShowActivity.class);
                            if (TodolistFragment.score==1000){
                                startActivity(context, intent, null);

                            }else if (TodolistFragment.score==2000){

                                startActivity(context, intent, null);

                            }else if (TodolistFragment.score==3000){

                                startActivity(context, intent, null);
                            }
                            if (isCheckClear==1){
                                return;
                            }
                            //체크한 카운트가 만든 리스트와 맞을때
                            if (checkSize == list.size()) {
                                insertIsCheckClear(context);//다완료할때 1집어늠.
                                TodolistFragment.score += 200;

                                Snackbar snackbar = Snackbar.make(TodayListFragment.todaylist_LinearLayout, "축하합니다! 모든 목표를 다 달성하셨습니다!\n보너스 경험치(+200exp)가 적립되었습니다!\n경험치 :" + TodolistFragment.score, Snackbar.LENGTH_INDEFINITE);
                                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                                Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
                                layout.setPadding(10, 10, 50, 10);
                                View snackbarView = snackbar.getView();
                                TextView tv = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                                tv.setTextColor(Color.WHITE);
                                tv.setTextSize(16);
                                tv.setMaxLines(3);
                                tv.setTextSize(16);
                                tv.setPadding(60, 60, 60, 60);
                                snackbarView.setBackgroundColor(Color.parseColor("#024873"));


                                snackbar.setAction("확인", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(context, ExpShowActivity.class);
                                        insertScore(TodolistFragment.score, context);
                                        startActivity(context, intent, null);
                                    }
                                });

                                snackbar.show();

                            }

                    }else{
                        strikeThroughPainting.clearStrikeThrough();

                    }
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        CheckBox todaylist_checkBox;
        ExTextView todaylist_text;
        TextView todaylist_area;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            todaylist_checkBox=itemView.findViewById(R.id.todaylist_checkBox);
            todaylist_text=itemView.findViewById(R.id.todaylist_text);
            todaylist_area=itemView.findViewById(R.id.todaylist_area);

        }
    }

//    checkBox True 인경우 함수화
    public void checkBoxTrue(StrikeThroughPainting strikeThroughPainting, final Context context, String taskText) {

        //현재 년,월,일
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        String year = new SimpleDateFormat("yyyy").format(date);
        String month = new SimpleDateFormat("MM").format(date);
        String day = new SimpleDateFormat("dd").format(date);

        final int currentYear = Integer.parseInt(year);
        final int currentMonth = Integer.parseInt(month);
        final int currentDay = Integer.parseInt(day);

        Log.d(TAG, "날짜 : " + currentYear + "년" + currentMonth + "월" + currentDay + "일");

        TodolistFragment.score += 100;

        //체크시 줄 생기는것
        strikeThroughPainting.color(Color.rgb(2, 72, 112))
                .strokeWidth(4).totalTime(10_0L).strikeThrough();
        //체크 True DB저장 , 스코어 저장
        insertScore(TodolistFragment.score, context);
        insertCheck(context, currentYear, currentMonth, currentDay, 1, taskText, area);


    }


    //스낵바 설정 함수화
    public void stnackBar(String txt) {
        Snackbar snackbar = Snackbar.make(TodayListFragment.todaylist_LinearLayout, txt + TodolistFragment.score, Snackbar.LENGTH_SHORT);
        snackbar.setActionTextColor(Color.parseColor("#ffffff"));
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
        layout.setPadding(10, 10, 50, 10);
        View snackbarView = snackbar.getView();
        TextView tv = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        tv.setTextSize(16);
        snackbarView.setBackgroundColor(Color.parseColor("#024873"));

        snackbar.show();
    }


    //DB 스코어 저장하기
    public void insertScore(int score, Context context) {
        db = DBHelper.getInstance(context).getWritableDatabase();

        Cursor cursor;
        cursor = db.rawQuery("SELECT * FROM profileTBL", null);
        String nickname = null;
        while (cursor.moveToNext()) {

            nickname = cursor.getString(0);
        }
        Log.d(TAG, "닉네임 확인 : " + nickname);


        db.execSQL("UPDATE profileTBL SET score=" + score + " WHERE NickName=" + "'" + nickname + "'" + ";");

        cursor.close();

        Log.d(TAG, "scoer DB 저장 : " + score);

    }

    // 체크 정보 업데이트 (0_false, 1_true)
    public void insertCheck(Context context, int currentYear, int currentMonth, int currentDay, int checkCount, String task, String groupName) {
        db = DBHelper.getInstance(context).getWritableDatabase();
        Log.d(TAG, "가져온 task : " + task);
        db.execSQL("UPDATE cleaningTBL SET year=" + currentYear + ", month =" + currentMonth + ", day=" + currentDay + ", checkCount=" + checkCount + " WHERE task ='" + task + "' AND area ='" + groupName + "' ;");
        Toast.makeText(context, "업데이트 저장되었습니다.", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "저장된 시간 : " + currentYear + currentMonth + currentDay);
        Log.d(TAG, "저장된 체크 : " + checkCount);
    }


    //체크 한 개수 가져오기
    public int selectCheckBoxCount(Context context, int year, int month, int day) {
        db = DBHelper.getInstance(context).getWritableDatabase();
        Cursor cursor;
//        cursor = db.rawQuery("SELECT checkCount FROM cleaningTBL WHERE checkCount=" + 1 + " AND year=" + year + " AND month=" + month + " AND day=" + day + ";", null);
        cursor = db.rawQuery("SELECT checkCount FROM cleaningTBL WHERE checkCount="+1+ " AND year=" + year + " AND month=" + month + " AND day=" + day + ";", null);
        int size = cursor.getCount();
        cursor.close();
        return size;
    }

//    //전체 저장 되어있는 리스트 사이즈
//    public int selectAllListSize(Context context) {
//        db = DBHelper.getInstance(context).getWritableDatabase();
//        Cursor cursor;
//        cursor = db.rawQuery("SELECT * FROM cleaningTBL;", null);
//        int allSize = cursor.getCount();
//        cursor.close();
//        Log.d(TAG, "총 리스트 사이즈 : " + allSize);
//        return allSize;
//    }

//    // 청소리스트 DB 삭제 부분
//    public void deleteCleningArea(String text, Context context) {
//        db = DBHelper.getInstance(context).getWritableDatabase();
//        db.execSQL("DELETE FROM cleaningTBL WHERE task='" + text + "';");
//        Log.d(TAG, "DB 삭제됨");
//
//    }

    //오늘 날짜에 청소 다 완료할때 업데이트(보너스 점수 막기용)
    public void insertIsCheckClear(Context context) {
        db = DBHelper.getInstance(context).getWritableDatabase();
        db.execSQL("INSERT INTO ischeckTBL (isCheckClear) VALUES (1) ;");
        Log.d(TAG, "DB ischeckClear 업데이트됨.");
    }

    //isCheckClear 가져오기
    public int selectIsCheckClear(Context context) {
        db = DBHelper.getInstance(context).getWritableDatabase();
        Cursor cursor;
        cursor = db.rawQuery("SELECT * FROM ischeckTBL", null);
        int ischeckClear = 0;
        while (cursor.moveToNext()) {
            ischeckClear = cursor.getInt(0);
        }
        Log.d(TAG, "ischeckClear 확인 : " + ischeckClear);

        return ischeckClear;
    }

    //체크정보 가져오기
    public int selectIsCheck(Context context, String groupName, String task) {
        db = DBHelper.getInstance(context).getWritableDatabase();
        Cursor cursor;
        cursor = db.rawQuery("SELECT checkCount FROM cleaningTBL WHERE task='" + task + "' AND area='" + groupName + "';", null);
        int count = 0;
        while (cursor.moveToNext()) {
            count = cursor.getInt(0);
        }
        return count;
    }


}
