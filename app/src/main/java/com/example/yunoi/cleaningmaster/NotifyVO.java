package com.example.yunoi.cleaningmaster;

public class NotifyVO {

    private int alarmId; // 알림리퀘스트번호
    private int year; //알림설정년
    private int month; //알림설정달
    private int day; //알림설정일
    private int hour; //알림시
    private int minute; //알림분
    private String area; // 청소구역
    private String task; // 청소내용
    private String alarmSet; //알림on/off
    private String loop; // 반복여부

    //청소구역 생성자
    public NotifyVO(int year, int month, int day, int hour, int minute, String area, String alarmSet, String loop) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.area = area;
        this.alarmSet = alarmSet;
        this.loop = loop;
    }

    public NotifyVO() {
    }

    // 알림 등록 생성자
    public NotifyVO(int alarmId, int year, int month, int day, int hour, int minute, String area, String task, String alarmSet, String loop) {
        this.alarmId = alarmId;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.area = area;
        this.task = task;
        this.alarmSet = alarmSet;
        this.loop = loop;
    }

    // 청소구역과 청소내용
    public NotifyVO(String area, String task) {
        this.area = area;
        this.task = task;
    }

    public int getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(int alarmId) {
        this.alarmId = alarmId;
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

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getAlarmSet() {
        return alarmSet;
    }

    public void setAlarmSet(String alarmSet) {
        this.alarmSet = alarmSet;
    }

    public String getLoop() {
        return loop;
    }

    public void setLoop(String loop) {
        this.loop = loop;
    }
}
