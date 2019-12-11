package com.example.yunoi.cleaningmaster;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static DBHelper dbHelper = null;

    private DBHelper(Context context) {
        super(context, "cleaningMasterDB", null, 1);
    }

    //    year 알림설정년
    //    month 알림설정달
    //    day 알림설정일
    //    hour 알림시
    //    minute 알림분
    //    area 청소구역
    //    task 청소내용
    //    alarmSet 알림on/off (boolean)
    //    check 달성여부 (boolean)
    //    roof 반복여부 (boolean)

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE notifyTBL (year INTEGER, month INTEGER, day INTEGER, hour INTEGER, minute INTEGER, area TEXT, task TEXT, alarmSet TEXT, roof TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS notifyTBL;");
        onCreate(db);
    }

    //싱글톤 패턴으로 구현
    public static DBHelper getInstance(Context context)
    {
        if( dbHelper == null)
        {
            dbHelper = new DBHelper(context);
        }

        return dbHelper;
    }
}
