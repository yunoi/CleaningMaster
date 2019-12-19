package com.example.yunoi.cleaningmaster;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CalendarFragment extends Fragment implements View.OnClickListener {
    ImageButton ibPrevious, ibNext;
    TextView tvMonth;
    GridView gvCalendar;
    private CalendarAdapter calendarAdapter;

    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.calendar, container, false);
        customActionBar(inflater);

        ibPrevious = view.findViewById(R.id.ibPrevious);
        ibNext = view.findViewById(R.id.ibNext);
        tvMonth = view.findViewById(R.id.tvMonth);
        gvCalendar = view.findViewById(R.id.gvCalendar);

        calendarAdapter = new CalendarAdapter(getContext());
        gvCalendar.setAdapter(calendarAdapter);

        setTvYearMonth();

        ibPrevious.setOnClickListener(this);
        ibNext.setOnClickListener(this);

        return view;
    }


    private void customActionBar(LayoutInflater inflater) {

        ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();

        // Custom Actionbar 사용하기 위해 CustomEnabled을 true 시키고 필요 없는 것은 false 시킨다
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);         //액션바 아이콘을 업 네비게이션 형태로 표시합니다.
        actionBar.setDisplayShowTitleEnabled(false);        //액션바에 표시되는 제목의 표시유무를 설정합니다.
        actionBar.setDisplayShowHomeEnabled(false);         //홈 아이콘을 숨김처리합니다.

        View actionbarlayout = inflater.inflate(R.layout.calendar_actionbar, null);
        actionBar.setCustomView(actionbarlayout);
        //액션바 양쪽 공백 없애기
        Toolbar parent = (Toolbar) actionbarlayout.getParent();
        parent.setContentInsetsAbsolute(0, 0);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);

        actionBar.setCustomView(actionbarlayout, params);

        ////end of 액션바 공백 없애기

        ImageButton acbar_backToMain = actionbarlayout.findViewById(R.id.acbar_backToMain);

        acbar_backToMain.setOnClickListener(this);

    }
    //상단에 년월 표시
    private void setTvYearMonth() {
        String yearMonth = String.valueOf(calendarAdapter.currentYear) + "년 " + String.valueOf(calendarAdapter.currentMonth + 1) + "월";
        tvMonth.setText(yearMonth);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.acbar_backToMain:
                Fragment mainFragment = new MainFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.coordinatorLayout, mainFragment).commit();
                break;
            case R.id.ibNext:
                calendarAdapter.setNextMonth();
                setTvYearMonth();
                calendarAdapter.notifyDataSetChanged();
                break;
            case R.id.ibPrevious:
                calendarAdapter.setPreviousMonth();
                setTvYearMonth();
                calendarAdapter.notifyDataSetChanged();
                break;
        }
    }
}