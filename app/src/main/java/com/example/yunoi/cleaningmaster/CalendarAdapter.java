package com.example.yunoi.cleaningmaster;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.jar.Attributes;

public class CalendarAdapter extends BaseAdapter {

    public Context context;
    public int layout;
    public CalendarDAO[] items;
    public LayoutInflater layoutInflater;


//    public CalendarAdapter(Context mContext) {
//        this.context = mContext;
//        this.layout = R.layout.calendar_item;
//    }
//
//    public CalendarAdapter(Context mContext, Attributes attributes) {
//        this.context = mContext;
//        this.layout = R.layout.calendar_item;
//    }
    public CalendarAdapter(Context context, int layout, CalendarDAO[] items) {
        this.context = context;
        this.layout = layout;
        this.items = items;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public Object getItem(int position) {
        return items[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if(view==null){
            view = layoutInflater.inflate(layout,viewGroup,false);
        }
        TextView tvCalendarDay = view.findViewById(R.id.tvCalendarDay);
        ImageView ivListResult = view.findViewById(R.id.ivListResult);


        final CalendarDAO item = items[position];

//        view.setTag(tvCalendarDay);


        int columnIndex = position % 7;

        if(columnIndex==0)
            tvCalendarDay.setTextColor(Color.RED);
        else if(columnIndex==6)
            tvCalendarDay.setTextColor(Color.BLUE);
        else
            tvCalendarDay.setTextColor(Color.BLACK);

        Calendar calendar = Calendar.getInstance();
        Integer iToday =calendar.get(Calendar.DAY_OF_MONTH);
        String sToday = String.valueOf(iToday);
        if (sToday.equals(getItem(position))) { //오늘 day 텍스트 컬러 변경
            tvCalendarDay.setTextColor(Color.GREEN);
        }
        if(item.getDay()==0){
            tvCalendarDay.setVisibility(View.INVISIBLE);
//            tvCalendarDay.setVisibility(View.VISIBLE);
//            tvCalendarDay.setTextColor(Color.GRAY);
        }else {
            tvCalendarDay.setVisibility(View.VISIBLE);
            tvCalendarDay.setText("" + item.getDay());

        }
        return view;
    }


}
