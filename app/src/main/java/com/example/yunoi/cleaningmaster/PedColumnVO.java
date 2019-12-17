package com.example.yunoi.cleaningmaster;

public class PedColumnVO {

    public int year;
    public int month;
    public int day;
    public String step;
    public String kcal;

    public PedColumnVO(int day, String step) {
        this.day = day;
        this.step = step;
    }

    public PedColumnVO(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public PedColumnVO(int year, int month, int day, String step, String kcal) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.step = step;
        this.kcal = kcal;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public String getStep() {
        return step;
    }

    public String getKcal() {
        return kcal;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public void setKcal(String kcal) {
        this.kcal = kcal;
    }
}
