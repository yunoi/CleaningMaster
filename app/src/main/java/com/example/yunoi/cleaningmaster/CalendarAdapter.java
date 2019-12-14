package com.example.yunoi.cleaningmaster;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class CalendarAdapter extends BaseAdapter {
     private ArrayList<String> list;
     private LayoutInflater layoutInflater;

    public CalendarAdapter(Context context, ArrayList<String> list) {
        this.list = list;
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if(view==null){
            view = layoutInflater.inflate(R.layout.calendar_item,viewGroup,false);
        }
        TextView tvCalendarDay = view.findViewById(R.id.tvCalendarDay);
        ImageView ivListResult = view.findViewById(R.id.ivListResult);

        view.setTag(tvCalendarDay);
        tvCalendarDay.setText("" + getItem(position));

        Calendar calendar = Calendar.getInstance();
        Integer iToday =calendar.get(Calendar.DAY_OF_MONTH);
        String sToday = String.valueOf(iToday);
        if (sToday.equals(getItem(position))) { //오늘 day 텍스트 컬러 변경
            tvCalendarDay.setTextColor(Color.BLUE);
        }
        return view;
    }
}
