package com.example.yunoi.cleaningmaster;

public class CalendarDAO {
    private int day;
    private int mark;

    public CalendarDAO(int day) {
        this.day = day;
    }

    public CalendarDAO(int day, int mark) {
        this.day = day;
        this.mark = mark;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }
}
