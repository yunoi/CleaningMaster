package com.example.yunoi.cleaningmaster;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntDef;
import android.util.Log;
import android.util.SparseBooleanArray;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class AlarmVO implements Parcelable {


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

    private final long id;
    private long time;
    private String label;
    private SparseBooleanArray allDays;
    private boolean isEnabled;
    private String area;

    private AlarmVO (Parcel in) {
        Log.i(getClass().getSimpleName(), "Creating database...");
        id = in.readLong();
        time = in.readLong();
        label = in.readString();
        allDays = in.readSparseBooleanArray();
        isEnabled = in.readByte() != 0;
        area = in.readString();

    }
    public static final Parcelable.Creator<AlarmVO> CREATOR = new Parcelable.Creator<AlarmVO>() {
        @Override
        public AlarmVO createFromParcel(Parcel in) {
            return new AlarmVO(in);
        }

        @Override
        public AlarmVO[] newArray(int size) {
            return new AlarmVO[size];
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
        parcel.writeString(area);
    }

    public AlarmVO() {
        this(NO_ID);
    }

    public AlarmVO(String label) {
        this.id = System.currentTimeMillis();
        this.label = label;
        this.allDays =  buildBaseDaysArray();
        this.isEnabled = false;

    }

    public AlarmVO(long id) {

        this(id, System.currentTimeMillis());
    }

    public AlarmVO(long id, long time, @Days int... days) {
        this(id, time, null, days);
    }

    public AlarmVO(long id, long time, String label, @Days int... days) {
        this.id = id;
        this.time = time;
        this.label = label;
        this.allDays = buildDaysArray(days);
    }

    public AlarmVO(long id, String label) {
        this.id = id;
        this.label = label;

    }

    public AlarmVO(long id, long time, String label, String area, @Days int... days) {
        this.id = id;
        this.time = time;
        this.label = label;
        this.allDays = buildDaysArray(days);
        this.area = area;

    }

    public AlarmVO(long id, long time, String label, SparseBooleanArray allDays, boolean isEnabled, String area) {
        this.id = id;
        this.time = time;
        this.label = label;
        this.allDays = allDays;
        this.isEnabled = isEnabled;
        this.area = area;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
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
                ", area='" + area + '\'' +
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
