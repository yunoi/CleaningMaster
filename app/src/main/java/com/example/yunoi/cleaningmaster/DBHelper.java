package com.example.yunoi.cleaningmaster;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static DBHelper dbHelper = null;

    private DBHelper(Context context) {
        super(context, "cleaningMasterDB", null, 2);
    }
    // notifyTBL: 알림관련 테이블
    // year 알림설정년 , month 알림설정달, day 알림설정일, hour 알림시, minute 알림분
    // area 청소구역, task 청소내용, alarmSet 알림on/off, loop 반복여부

    // cleaningTBL: 청소, 점수, 달성 관련 테이블
    // year 달성년 , month 달성달, day 달성일, area 청소구역, task 청소내용, taskCount 청소리스트개수, checkCount 달성한 청소 갯수
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE notifyTBL (year INTEGER, month INTEGER, day INTEGER, hour INTEGER, minute INTEGER, area TEXT, task TEXT, alarmSet TEXT, loop TEXT);");
        db.execSQL("CREATE TABLE cleaningTBL (year INTEGER, month INTEGER, day INTEGER, area TEXT, task TEXT,  taskCount INTEGER, checkCount INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS notifyTBL;");
        db.execSQL("DROP TABLE IF EXISTS cleaningTBL;");
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
