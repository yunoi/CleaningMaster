package com.example.yunoi.cleaningmaster;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.jar.Attributes;

public class CalendarAdapter extends BaseAdapter {

    public Context context;
    public int layout;
    public ArrayList<CalendarDAO> items;
    public LayoutInflater layoutInflater;
    private SQLiteDatabase db;
    ArrayList<Integer> yearList = new ArrayList<Integer>();
    ArrayList<Integer> monthList = new ArrayList<Integer>();
    ArrayList<Integer> dayList = new ArrayList<Integer>();

    int size1,size2,cursorYear,cursorMonth,cursorDay;
    //    public CalendarAdapter(Context mContext) {
//        this.context = mContext;
//        this.layout = R.layout.calendar_item;
//    }
//
//    public CalendarAdapter(Context mContext, Attributes attributes) {
//        this.context = mContext;
//        this.layout = R.layout.calendar_item;
//    }
    public CalendarAdapter(Context context, int layout, ArrayList<CalendarDAO> items) {
        this.context = context;
        this.layout = layout;
        this.items = items;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = layoutInflater.inflate(layout, viewGroup, false);
        }
        TextView tvCalendarDay = view.findViewById(R.id.tvCalendarDay);
        ImageView ivListResult = view.findViewById(R.id.ivListResult);

        final CalendarDAO item = items.get(position);

//        view.setTag(tvCalendarDay);


        int columnIndex = position % 7;

        if (columnIndex == 0)
            tvCalendarDay.setTextColor(Color.RED);
        else if (columnIndex == 6)
            tvCalendarDay.setTextColor(Color.BLUE);
        else
            tvCalendarDay.setTextColor(Color.BLACK);

        Calendar calendar = Calendar.getInstance();
        int iToday = calendar.get(Calendar.DAY_OF_MONTH);
        String sToday = String.valueOf(iToday);
        if (sToday.equals(getItem(position))) { //오늘 day 텍스트 컬러 변경
            tvCalendarDay.setTextColor(Color.GREEN);
        }
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
//        Log.d("tbl1",String.valueOf(year));
//        Log.d("tbl2",String.valueOf(month));
//        Log.d("tbl3",String.valueOf(iToday));
        db = DBHelper.getInstance(context.getApplicationContext()).getWritableDatabase();
        //오늘 날의 성공한 리스트 갯수
        Cursor cursor = db.rawQuery("SELECT checkCount FROM cleaningTBL WHERE checkCount=" + 1 + " AND year=" + year + " AND month=" + (month + 1) + " AND day=" + iToday + ";", null);
//        Log.d("tbl1",cursor.getString(cursor.getCount()));
        //오늘의 리스트 총 갯수
        Cursor cursor1 = db.rawQuery("SELECT checkCount FROM cleaningTBL WHERE year=" + year + " AND month=" + (month + 1) + " AND day=" + iToday + ";", null);
        Cursor cursor2 = db.rawQuery("SELECT year,month,day FROM cleaningTBL ;", null);
        size1 = cursor.getCount();
        size2 = cursor1.getCount();

        while (cursor2.moveToNext()) {
            cursorYear = cursor2.getInt(cursor2.getColumnIndex("year"));
            cursorMonth = cursor2.getInt(cursor2.getColumnIndex("month"));
            cursorDay = cursor2.getInt(cursor2.getColumnIndex("day"));
            yearList.add(cursorYear);
            monthList.add(cursorMonth);
            dayList.add(cursorDay);
//            Log.d("cal1",String.valueOf(cursorYear));
//            Log.d("cal2",String.valueOf(cursorMonth));
//            Log.d("cal3",String.valueOf(cursorDay));
        }

        for (int i = 0; (i < dayList.size()); i++) {
//            Log.d("cal4",String.valueOf(dayList.get(i)));
            if ((yearList.get(i)<=year)&&(monthList.get(i)<=(month+1))&&(dayList.get(i)<=iToday)) {
//                Log.d("cal5",String.valueOf(dayList.get(i)));
                if ((yearList.get(i)<=year)&&(monthList.get(i)<=(month+1))&&(dayList.get(i)<=iToday)&&(dayList.get(i) == (position - 6))) {
                    if (size1 == size2) {
                        ivListResult.setVisibility(View.VISIBLE);
                        ivListResult.setImageResource(R.drawable.success_32);
                        ivListResult.setAlpha(40);
                    } else if (size1 != size2) {
                        ivListResult.setVisibility(View.VISIBLE);
                        ivListResult.setImageResource(R.drawable.fail_32);
                        ivListResult.setAlpha(40);
                    } else if (size1 == 0 && size2 == 0) {
                        ivListResult.setVisibility(View.INVISIBLE);
                    }
                }
            }
        }

        cursor.close();
        cursor1.close();
        cursor2.close();
        //달력에 요일 표시 및 날짜 표시-DB연결 X.
        if (item.getDay() < 0) {
            ivListResult.setVisibility(View.INVISIBLE);
            switch (item.getDay()) {
                case -1:
                    tvCalendarDay.setVisibility(View.VISIBLE);
                    tvCalendarDay.setText("일");
                    break;
                case -2:
                    tvCalendarDay.setVisibility(View.VISIBLE);
                    tvCalendarDay.setText("월");
                    break;
                case -3:
                    tvCalendarDay.setVisibility(View.VISIBLE);
                    tvCalendarDay.setText("화");
                    break;
                case -4:
                    tvCalendarDay.setVisibility(View.VISIBLE);
                    tvCalendarDay.setText("수");
                    break;
                case -5:
                    tvCalendarDay.setVisibility(View.VISIBLE);
                    tvCalendarDay.setText("목");
                    break;
                case -6:
                    tvCalendarDay.setVisibility(View.VISIBLE);
                    tvCalendarDay.setText("금");
                    break;
                case -7:
                    tvCalendarDay.setVisibility(View.VISIBLE);
                    tvCalendarDay.setText("토");
                    break;

            }
        } else if (item.getDay() == 0) {
            tvCalendarDay.setVisibility(View.INVISIBLE);
            ivListResult.setVisibility(View.INVISIBLE);
        } else {
            tvCalendarDay.setVisibility(View.VISIBLE);
            tvCalendarDay.setText("" + item.getDay());
        }
        return view;
    }
}