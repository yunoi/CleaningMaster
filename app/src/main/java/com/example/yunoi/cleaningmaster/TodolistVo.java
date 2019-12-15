package com.example.yunoi.cleaningmaster;

public class TodolistVo {

    private int _id; // 기본키 자동증가
    private int year;   // 달성년
    private int month;  // 달성달
    private int day;    // 달성일
    private String groupName;   // 청소구역
    private String todolist_text;   // 청소내용
    private int checkcount; // 달성여부
    private int score;  // 점수
    private int alarmState; // 알림스위치 조작용 onoff
    private int alarmYear; //알림설정년
    private int alarmMonth; //알림설정달
    private int alarmDay; //알림설정일
    private int alarmHour; //알림시
    private int alarmMinute; //알림분
    private int loop; // 반복여부(요일 1~7)
    private int mon;
    private int tue;
    private int wed;
    private int thu;
    private int fri;
    private int sat;
    private int sun;

    public TodolistVo() {
    }


    public TodolistVo(int year, int month, int day, String groupName, String todolist_text, int checkcount) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.groupName = groupName;
        this.todolist_text = todolist_text;
        this.checkcount = checkcount;
    }

    public TodolistVo(int year, int month, int day, String groupName, String todolist_text, int checkcount, int alarmState) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.groupName = groupName;
        this.todolist_text = todolist_text;
        this.checkcount = checkcount;
        this.alarmState = alarmState;
    }

    public TodolistVo(String todolist_text) {
        this.todolist_text = todolist_text;
    }

    public TodolistVo(String todolist_text, int checkcount) {
        this.todolist_text = todolist_text;
        this.checkcount = checkcount;
    }

    public TodolistVo(String groupName, String todolist_text, int alarmState, int alarmYear, int alarmMonth, int alarmDay, int alarmHour, int alarmMinute, int loop) {
        this.groupName = groupName;
        this.todolist_text = todolist_text;
        this.alarmState = alarmState;
        this.alarmYear = alarmYear;
        this.alarmMonth = alarmMonth;
        this.alarmDay = alarmDay;
        this.alarmHour = alarmHour;
        this.alarmMinute = alarmMinute;
        this.loop = loop;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getTodolist_text() {
        return todolist_text;
    }

    public void setTodolist_text(String todolist_text) {
        this.todolist_text = todolist_text;
    }

    public int getCheckcount() {
        return checkcount;
    }

    public void setCheckcount(int checkcount) {
        this.checkcount = checkcount;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getAlarmState() {
        return alarmState;
    }

    public void setAlarmState(int alarmState) {
        this.alarmState = alarmState;
    }

    public int getAlarmYear() {
        return alarmYear;
    }

    public void setAlarmYear(int alarmYear) {
        this.alarmYear = alarmYear;
    }

    public int getAlarmMonth() {
        return alarmMonth;
    }

    public void setAlarmMonth(int alarmMonth) {
        this.alarmMonth = alarmMonth;
    }

    public int getAlarmDay() {
        return alarmDay;
    }

    public void setAlarmDay(int alarmDay) {
        this.alarmDay = alarmDay;
    }

    public int getAlarmHour() {
        return alarmHour;
    }

    public void setAlarmHour(int alarmHour) {
        this.alarmHour = alarmHour;
    }

    public int getAlarmMinute() {
        return alarmMinute;
    }

    public void setAlarmMinute(int alarmMinute) {
        this.alarmMinute = alarmMinute;
    }

    public int getLoop() {
        return loop;
    }

    public void setLoop(int loop) {
        this.loop = loop;
    }

    public int getMon() {
        return mon;
    }

    public void setMon(int mon) {
        this.mon = mon;
    }

    public int getTue() {
        return tue;
    }

    public void setTue(int tue) {
        this.tue = tue;
    }

    public int getWed() {
        return wed;
    }

    public void setWed(int wed) {
        this.wed = wed;
    }

    public int getThu() {
        return thu;
    }

    public void setThu(int thu) {
        this.thu = thu;
    }

    public int getFri() {
        return fri;
    }

    public void setFri(int fri) {
        this.fri = fri;
    }

    public int getSat() {
        return sat;
    }

    public void setSat(int sat) {
        this.sat = sat;
    }

    public int getSun() {
        return sun;
    }

    public void setSun(int sun) {
        this.sun = sun;
    }
}