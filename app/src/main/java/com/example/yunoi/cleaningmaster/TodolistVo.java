package com.example.yunoi.cleaningmaster;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntDef;
import android.util.Log;
import android.util.SparseBooleanArray;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class TodolistVo implements Parcelable{

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

    public TodolistVo(int checkcount) {
        this.checkcount = checkcount;
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

    //////////////////////////////////////////////// 알람 ///////////////////////////////////////////////

    private TodolistVo(Parcel in) {
        Log.i(getClass().getSimpleName(), "Creating database...");
        id = in.readLong();
        time = in.readLong();
        label = in.readString();
        allDays = in.readSparseBooleanArray();
        isEnabled = in.readByte() != 0;

    }

    public static final Parcelable.Creator<TodolistVo> CREATOR = new Parcelable.Creator<TodolistVo>() {
        @Override
        public TodolistVo createFromParcel(Parcel in) {
            return new TodolistVo(in);
        }

        @Override
        public TodolistVo[] newArray(int size) {
            return new TodolistVo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeLong(time);
        parcel.writeString(label);
        parcel.writeSparseBooleanArray(allDays);
        parcel.writeByte((byte) (isEnabled ? 1 : 0));

    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({MON,TUE,WED,THU,FRI,SAT,SUN})
    @interface Days{}
    public static final int MON = 1;
    public static final int TUE = 2;
    public static final int WED = 3;
    public static final int THU = 4;
    public static final int FRI = 5;
    public static final int SAT = 6;
    public static final int SUN = 7;

    private static final long NO_ID = -1;

    private long id;
    private long time;
    private String label;
    private SparseBooleanArray allDays;
    private boolean isEnabled;

    public TodolistVo() {
        this(NO_ID);
    }

    public TodolistVo(long id) {
        this(id, System.currentTimeMillis());
    }

    public TodolistVo(long id, long time, @Days int... days) {
        this(id, time, null, days);
    }

    public TodolistVo(long id, long time, String label, @Days int... days) {
        this.id = id;
        this.time = time;
        this.label = label;
        this.allDays = buildDaysArray(days);
    }

    public long getId() {
        return id;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getTime() {
        return time;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setDay(@Days int day, boolean isAlarmed) {
        allDays.append(day, isAlarmed);
    }

    public SparseBooleanArray getDays() {
        return allDays;
    }

    public boolean getDay(@Days int day){
        return allDays.get(day);
    }

    public void setIsEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public String toString() {
        return "Alarm{" +
                "id=" + id +
                ", time=" + time +
                ", label='" + label + '\'' +
                ", allDays=" + allDays +
                ", isEnabled=" + isEnabled + "}'";
    }

    public int notificationId() {
        final long id = getId();
        return (int) (id^(id>>>32));
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (int) (id^(id>>>32));
        result = 31 * result + (int) (time^(time>>>32));
        result = 31 * result + label.hashCode();
        for(int i = 0; i < allDays.size(); i++) {
            result = 31 * result + (allDays.valueAt(i)? 1 : 0);
        }
        return result;
    }

    private static SparseBooleanArray buildDaysArray(@Days int... days) {

        final SparseBooleanArray array = buildBaseDaysArray();

        for (@Days int day : days) {
            array.append(day, true);
        }

        return array;

    }

    private static SparseBooleanArray buildBaseDaysArray() {

        final int numDays = 7;

        final SparseBooleanArray array = new SparseBooleanArray(numDays);

        array.put(MON, false);
        array.put(TUE, false);
        array.put(WED, false);
        array.put(THU, false);
        array.put(FRI, false);
        array.put(SAT, false);
        array.put(SUN, false);

        return array;

    }
}
