package com.example.yunoi.cleaningmaster;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.util.SparseBooleanArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public final class AlarmUtils {

    private static final SimpleDateFormat TIME_FORMAT =
            new SimpleDateFormat("h:mm", Locale.getDefault());
    private static final SimpleDateFormat AM_PM_FORMAT =
            new SimpleDateFormat("a", Locale.getDefault());

    private static final int REQUEST_ALARM = 1;
    private static final String[] PERMISSIONS_ALARM = {
            Manifest.permission.VIBRATE
    };

    private AlarmUtils() {
        throw new AssertionError();
    }

    public static void checkAlarmPermissions(Activity activity) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }

        final int permission = ActivityCompat.checkSelfPermission(
                activity, Manifest.permission.VIBRATE
        );

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_ALARM,
                    REQUEST_ALARM
            );
        }

    }

    public static ContentValues toContentValues(AlarmVO alarm) {

        final ContentValues cv = new ContentValues(13);

        cv.put(DBHelper.COL_TIME, alarm.getTime());
        cv.put(DBHelper.COL_LABEL, alarm.getLabel());

        final SparseBooleanArray days = alarm.getDays();
        cv.put(DBHelper.COL_MON, days.get(AlarmVO.MON) ? 1 : 0);
        cv.put(DBHelper.COL_TUE, days.get(AlarmVO.TUE) ? 1 : 0);
        cv.put(DBHelper.COL_WED, days.get(AlarmVO.WED) ? 1 : 0);
        cv.put(DBHelper.COL_THU, days.get(AlarmVO.THU) ? 1 : 0);
        cv.put(DBHelper.COL_FRI, days.get(AlarmVO.FRI) ? 1 : 0);
        cv.put(DBHelper.COL_SAT, days.get(AlarmVO.SAT) ? 1 : 0);
        cv.put(DBHelper.COL_SUN, days.get(AlarmVO.SUN) ? 1 : 0);

        cv.put(DBHelper.COL_IS_ENABLED, alarm.isEnabled());
        cv.put(DBHelper.COL_AREA, alarm.getArea());

        return cv;

    }

    public static ArrayList<AlarmVO> buildAlarmList(Cursor c) {

        if (c == null) return new ArrayList<>();

        final int size = c.getCount();

        final ArrayList<AlarmVO> alarms = new ArrayList<>(size);

        if (c.moveToFirst()) {
            do {

                final long id = c.getLong(c.getColumnIndex(DBHelper._ID));
                final long time = c.getLong(c.getColumnIndex(DBHelper.COL_TIME));
                final String label = c.getString(c.getColumnIndex(DBHelper.COL_LABEL));
                final boolean mon = c.getInt(c.getColumnIndex(DBHelper.COL_MON)) == 1;
                final boolean tue = c.getInt(c.getColumnIndex(DBHelper.COL_TUE)) == 1;
                final boolean wed = c.getInt(c.getColumnIndex(DBHelper.COL_WED)) == 1;
                final boolean thu = c.getInt(c.getColumnIndex(DBHelper.COL_THU)) == 1;
                final boolean fri = c.getInt(c.getColumnIndex(DBHelper.COL_FRI)) == 1;
                final boolean sat = c.getInt(c.getColumnIndex(DBHelper.COL_SAT)) == 1;
                final boolean sun = c.getInt(c.getColumnIndex(DBHelper.COL_SUN)) == 1;
                final boolean isEnabled = c.getInt(c.getColumnIndex(DBHelper.COL_IS_ENABLED)) == 1;
                final String area = c.getString(c.getColumnIndex(DBHelper.COL_AREA));

                final AlarmVO alarm = new AlarmVO(id, time, label, area);
                alarm.setDay(AlarmVO.MON, mon);
                alarm.setDay(AlarmVO.TUE, tue);
                alarm.setDay(AlarmVO.WED, wed);
                alarm.setDay(AlarmVO.THU, thu);
                alarm.setDay(AlarmVO.FRI, fri);
                alarm.setDay(AlarmVO.SAT, sat);
                alarm.setDay(AlarmVO.SUN, sun);

                alarm.setIsEnabled(isEnabled);
                alarm.setArea(area);

                alarms.add(alarm);

            } while (c.moveToNext());
        }

        return alarms;

    }

    public static String getReadableTime(long time) {
        return TIME_FORMAT.format(time);
    }

    public static String getAmPm(long time) {
        return AM_PM_FORMAT.format(time);
    }

    public static boolean isAlarmActive(AlarmVO
                                                alarm) {

        final SparseBooleanArray days = alarm.getDays();

        boolean isActive = false;
        int count = 0;

        while (count < days.size() && !isActive) {
            isActive = days.valueAt(count);
            count++;
        }

        return isActive;

    }

    public static String getActiveDaysAsString(AlarmVO alarm) {

        StringBuilder builder = new StringBuilder("Active Days: ");

        if (alarm.getDay(AlarmVO.MON)) builder.append("Monday, ");
        if (alarm.getDay(AlarmVO.TUE)) builder.append("Tuesday, ");
        if (alarm.getDay(AlarmVO.WED)) builder.append("Wednesday, ");
        if (alarm.getDay(AlarmVO.THU)) builder.append("Thursday, ");
        if (alarm.getDay(AlarmVO.FRI)) builder.append("Friday, ");
        if (alarm.getDay(AlarmVO.SAT)) builder.append("Saturday, ");
        if (alarm.getDay(AlarmVO.SUN)) builder.append("Sunday.");

        if (builder.substring(builder.length() - 2).equals(", ")) {
            builder.replace(builder.length() - 2, builder.length(), ".");
        }

        return builder.toString();

    }
}
