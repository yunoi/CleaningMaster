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
    Calendar calendar;

    public CalendarDAO[] items;
    public Calendar mCalendar;
    public int firstDay;
    public int mStartDay;
    public int startDay;
    public int currentYear;
    public int currentMonth;
    public int lastDay;
    public int selectedPosition = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.calendar, container, false);
        customActionBar(inflater);

        ibPrevious = view.findViewById(R.id.ibPrevious);
        ibNext = view.findViewById(R.id.ibNext);
        tvMonth = view.findViewById(R.id.tvMonth);
        gvCalendar = view.findViewById(R.id.gvCalendar);

        init();
        setTvYearMonth();


        calendarAdapter = new CalendarAdapter(getContext(),R.layout.calendar_item,items);
        gvCalendar.setAdapter(calendarAdapter);

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
                setNextMonth();
                setTvYearMonth();
                calendarAdapter.notifyDataSetChanged();
                break;
            case R.id.ibPrevious:
                setPreviousMonth();
                setTvYearMonth();
                calendarAdapter.notifyDataSetChanged();
                break;
        }
    }

    private void init() {
        items = new CalendarDAO[42];
        mCalendar = Calendar.getInstance();

        recalculate();
        resetDayNumbers();
    }

    private void setTvYearMonth() {
        String yearMonth = String.valueOf(currentYear) + "년 " + String.valueOf(currentMonth + 1) + "월";
        Log.d("Date1", String.valueOf(currentYear));
        Log.d("Date1", String.valueOf(currentMonth));
        tvMonth.setText(yearMonth);
    }

    private void recalculate() {
        mCalendar.set(Calendar.DAY_OF_MONTH, 1);
        int dayOfWeek = mCalendar.get(Calendar.DAY_OF_WEEK);

        firstDay = getFirstDay(dayOfWeek);
        mStartDay = mCalendar.getFirstDayOfWeek();
        currentYear = mCalendar.get(Calendar.YEAR);
        Log.d("Date", String.valueOf(currentYear));
        currentMonth = mCalendar.get(Calendar.MONTH);
        Log.d("Date", String.valueOf(currentMonth));
        lastDay = getMonthLastDay(currentYear, currentMonth);
        startDay = getFirstDayOfWeek();


    }


    private int getFirstDay(int dayOfWeek) {
        int firstDay = 0;
        switch (dayOfWeek) {
            case Calendar.SUNDAY:
                firstDay = 0;
                break;
            case Calendar.MONDAY:
                firstDay = 1;
                break;
            case Calendar.TUESDAY:
                firstDay = 2;
                break;
            case Calendar.WEDNESDAY:
                firstDay = 3;
                break;
            case Calendar.THURSDAY:
                firstDay = 4;
                break;
            case Calendar.FRIDAY:
                firstDay = 5;
                break;
            case Calendar.SATURDAY:
                firstDay = 6;
                break;
        }

        return firstDay;

    }

    private int getMonthLastDay(int currentYear, int currentMonth) {
        switch (currentMonth + 1) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;

            case 4:
            case 6:
            case 9:
            case 11:
                return 30;

            default:
                if ((currentYear % 4 == 0) && (currentYear % 100 != 0) || currentYear % 400 == 0) {
                    return 29;
                } else {
                    return 28;
                }
        }
    }

    private int getFirstDayOfWeek() {
        int startDay = Calendar.getInstance().getFirstDayOfWeek();
        switch (startDay) {
            case Calendar.SATURDAY:
                return Time.SATURDAY;
            case Calendar.MONDAY:
                return Time.MONDAY;
            case Calendar.SUNDAY:
                return Time.SUNDAY;
        }
        return 0;
    }

    private void resetDayNumbers() {
        for (int i = 0; i < 42; i++) {
            int dayNum = (i + 1) - firstDay;
            if (dayNum < 1 || dayNum > lastDay) {
                dayNum = 0;
            }
            items[i] = new CalendarDAO(dayNum);
        }
    }

    public void setPreviousMonth() {
        mCalendar.add(Calendar.MONTH, -1);
        recalculate();
        resetDayNumbers();
        selectedPosition = -1;
    }

    public void setNextMonth() {
        mCalendar.add(Calendar.MONTH, 1);
        recalculate();
        resetDayNumbers();
        selectedPosition = -1;
    }

}