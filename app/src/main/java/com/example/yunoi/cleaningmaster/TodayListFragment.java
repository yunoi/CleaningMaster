package com.example.yunoi.cleaningmaster;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TodayListFragment extends Fragment {


    private View view;
    private SQLiteDatabase db;
    private RecyclerView recyclerView;
    private ArrayList<TodayListVO> list=new ArrayList<TodayListVO>();
    private LinearLayoutManager linearLayoutManager;
    private TodayListAdapter todayListAdapter;
    public static LinearLayout todaylist_LinearLayout;
    private static final String TAG="확인";
    private String today;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.todaylist_layout, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.todaylit_recylerView);
        todaylist_LinearLayout=view.findViewById(R.id.todaylist_LinearLayout);

        //액션바 설정
        ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        // Custom Actionbar를 사용하기 위해 CustomEnabled을 true 시키고 필요 없는 것은 false 시킨다
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);            //액션바 아이콘을 업 네비게이션 형태로 표시합니다.
        actionBar.setDisplayShowTitleEnabled(false);        //액션바에 표시되는 제목의 표시유무를 설정합니다.
        actionBar.setDisplayShowHomeEnabled(false);//홈 아이콘을 숨김처리합니다.

        View actionbarlayout = inflater.inflate(R.layout.todaylist_actionbar_layout, null);
        actionBar.setCustomView(actionbarlayout);
        //액션바 양쪽 공백 없애기
        Toolbar parent = (Toolbar) actionbarlayout.getParent();
        parent.setContentInsetsAbsolute(0, 0);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(actionbarlayout, params);

        //end of 액션바 공백 없애기

        //액션바 버튼
        ImageButton imageButton = actionbarlayout.findViewById(R.id.actionbar_todayBtnBack);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new MainFragment(); //돌아가는 fragment
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.coordinatorLayout, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });




        //리싸이클러뷰 설정
        linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        todayListAdapter=new TodayListAdapter(R.layout.todaylist_holder_layout,list);
        recyclerView.setAdapter(todayListAdapter);

        selectCleaningArea ();

        return view;
    }


    //저장된 DB 내용 가져오기 (할일,체크박스 true,false)
    public void selectCleaningArea(){
        Calendar calendar = Calendar.getInstance();
        String date=getStartIndexFromTime(calendar);
        db = DBHelper.getInstance(getActivity().getApplicationContext()).getWritableDatabase();
        Cursor cursor;
        cursor = db.rawQuery("SELECT distinct cleaningTBL.task, cleaningTBL.checkCount, cleaningTBL.area FROM cleaningTBL INNER JOIN alarmTBL ON cleaningTBL.task = alarmTBL.task WHERE alarmTBL."+date+" = 1;", null);
        list.clear();
        while (cursor.moveToNext()) {

            list.add(new TodayListVO(cursor.getString(0),cursor.getInt(1),cursor.getString(2)));
            Log.d(TAG,"DB에서 select함 내용 : "+ cursor.getString(0)+" / check : "+cursor.getInt(1)+" /  area : "+cursor.getString(2));

        }

        todayListAdapter.notifyDataSetChanged();
        cursor.close();
        Log.d(TAG,"DB에서 select함");
    }

    private static String getStartIndexFromTime(Calendar c) {
        Log.i(TAG, "getStartIndexFromTime...");
        final int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

        String startIndex = null;
        switch (dayOfWeek) {
            case Calendar.MONDAY:
                startIndex = "mon";
                break;
            case Calendar.TUESDAY:
                startIndex = "tue";
                break;
            case Calendar.WEDNESDAY:
                startIndex = "wed";
                break;
            case Calendar.THURSDAY:
                startIndex = "thu";
                break;
            case Calendar.FRIDAY:
                startIndex = "fri";
                break;
            case Calendar.SATURDAY:
                startIndex = "sat";
                break;
            case Calendar.SUNDAY:
                startIndex = "sun";
                break;
        }

        return startIndex;

    }

}
