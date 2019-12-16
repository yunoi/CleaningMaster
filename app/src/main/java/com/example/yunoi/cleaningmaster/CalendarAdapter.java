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
        Log.d("tbl1",String.valueOf(year));
        Log.d("tbl2",String.valueOf(month));
        Log.d("tbl3",String.valueOf(iToday));
        db = DBHelper.getInstance(context.getApplicationContext()).getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT checkCount FROM cleaningTBL WHERE checkCount="+1+" AND year="+year+" AND month="+(month+1)+" AND day="+iToday+";", null);
        cursor.moveToFirst();
//        Log.d("tbl1",cursor.getString(cursor.getCount()));
        Cursor cursor1 = db.rawQuery("SELECT * FROM cleaningTBL ;", null);
//        Log.d("tbl2",cursor1.getString(cursor1.getCount()));
        int size=cursor.getCount();
        int size2=cursor1.getCount();
        int cursorDay=cursor1.getInt(cursor1.getColumnIndex("day"));
        Log.d("tbl4",String.valueOf(size));
        Log.d("tbl5",String.valueOf(size2));
        cursor.close();
        cursor1.close();
        if((size==size2)&&(cursorDay==iToday)){
            ivListResult.setVisibility(View.VISIBLE);
            ivListResult.setImageResource(R.drawable.add);
            ivListResult.setAlpha(50);
        }else if(size!=size2&&(cursorDay==iToday)){
            ivListResult.setVisibility(View.VISIBLE);
            ivListResult.setImageResource(R.drawable.backbutton);
            ivListResult.setAlpha(50);
        }else if(size2==0&&(cursorDay==iToday)){
            ivListResult.setVisibility(View.INVISIBLE);
        }
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
//            tvCalendarDay.setVisibility(View.VISIBLE);
//            tvCalendarDay.setTextColor(Color.GRAY);
        } else {
            tvCalendarDay.setVisibility(View.VISIBLE);
            tvCalendarDay.setText("" + item.getDay());
        }
        return view;
    }
}
