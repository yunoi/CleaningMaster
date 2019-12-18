package com.example.yunoi.cleaningmaster;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    ArrayList<String> list = new ArrayList<>();
    ArrayList<String> listData = new ArrayList<>();

    private static DBHelper dbHelper = null;

    private static final String TABLE_NAME = "alarmTBL";

    public static final String _ID = "_id";
    public static final String COL_TIME = "time";
    public static final String COL_LABEL = "task";
    public static final String COL_MON = "mon";
    public static final String COL_TUE = "tue";
    public static final String COL_WED = "wed";
    public static final String COL_THU = "thu";
    public static final String COL_FRI = "fri";
    public static final String COL_SAT = "sat";
    public static final String COL_SUN = "sun";
    public static final String COL_IS_ENABLED = "alarmState";

    private DBHelper(Context context) {
        super(context, "cleaningMasterDB", null, 15);
    }
    // notifyTBL: 알림관련 테이블
    // alarmId 알림리퀘스트번호, year 알림설정년 , month 알림설정달, day 알림설정일, hour 알림시, minute 알림분
    // area 청소구역, task 청소내용, alarmSet 알림on/off, loop 반복여부

    // cleaningTBL: 청소, 점수, 달성 관련 테이블
    // year 달성년 , month 달성달, day 달성일, area 청소구역, task 청소내용, taskCount 청소리스트개수, checkCount 달성한 청소 갯수

    // areaTBL : 구역 이름 만 (메인 리스트 만들때)

    // profileTBL: 프로필(키,몸무게 등) 관련 테이블
    // NickName 닉네임 , Score 점수, Rank 등급, , Gender 성별, Height 키, Weight 몸무게, Age 나이, CONSTRAINT PK_Customer PRIMARY KEY (NickName)

    // PedTBL : 년 월 일 걸음 칼로리 관련된 테이블
    // year 년 month , 월 day 일 <= INTEGER step 걸음 kcal 칼로리 <= TEXT
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE cleaningTBL (_id INTEGER PRIMARY KEY AUTOINCREMENT, year INTEGER, month INTEGER, day INTEGER, area TEXT, task TEXT, checkCount INTEGER, score INTEGER," +
                " alarmState INTEGER, alarmYear INTEGER, alarmMonth INTEGER, alarmDay INTEGER, alarmHour INTEGER, alarmMinute INTEGER, time INTEGER, loop INTEGER," +
                " mon INTEGER, tue INTEGER, wed INTEGER, thu INTEGER, fri INTEGER, sat INTEGER, sun INTEGER);");
        db.execSQL("CREATE TABLE areaTBL (area TEXT);");
        db.execSQL("CREATE TABLE profileTBL (NickName TEXT PRIMARY KEY, Score INTEGER, Rank TEXT ,Gender TEXT, Height REAL, Weight REAL, Age INTEGER);");
        db.execSQL("CREATE TABLE ischeckTBL (isCheckClear INTEGER);");
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TIME + " INTEGER NOT NULL, " +
                COL_LABEL + " TEXT, " +
                COL_MON + " INTEGER NOT NULL, " +
                COL_TUE + " INTEGER NOT NULL, " +
                COL_WED + " INTEGER NOT NULL, " +
                COL_THU + " INTEGER NOT NULL, " +
                COL_FRI + " INTEGER NOT NULL, " +
                COL_SAT + " INTEGER NOT NULL, " +
                COL_SUN + " INTEGER NOT NULL, " +
                COL_IS_ENABLED + " INTEGER NOT NULL)" );
        db.execSQL("CREATE TABLE PedTBL (year INTEGER , month INTEGER , day INTEGER ,step TEXT , kcal TEXT ,PRIMARY KEY (year   ,month,   day))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS cleaningTBL;");
        db.execSQL("DROP TABLE IF EXISTS areaTBL;");
        db.execSQL("DROP TABLE IF EXISTS profileTBL;");
        db.execSQL("DROP TABLE IF EXISTS ischeckTBL;");
        db.execSQL("DROP TABLE IF EXISTS alarmTBL;");
        db.execSQL("DROP TABLE IF EXISTS PedTBL");
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
    //PedTBL 만보기
    public void insert(PedColumnVO pedColumnVO){

        int year=pedColumnVO.getYear();
        int month=pedColumnVO.getMonth();
        int day=pedColumnVO.getDay();
        String uStep=pedColumnVO.getStep();
        String uKcal=pedColumnVO.getKcal();

        // DB 오픈
        SQLiteDatabase sqldb = getWritableDatabase();
        sqldb.execSQL("INSERT INTO PedTBL(year,month,day,step,kcal)"+
                "VALUES(" + year + "," + month +"," + day + "," + uStep + "," +uKcal+",");
        // DB를 사용후 종료시키기
        sqldb.close();
    }
    public ArrayList<String>getResult(PedColumnVO pedColumnVO){

        SQLiteDatabase db = getWritableDatabase();
        //cursor=db.rawQuery("SELECT step FROM PedTBL;",null);
        int year=pedColumnVO.getYear();
        int month=pedColumnVO.getMonth();
        int day=pedColumnVO.getDay();
        // 년 월 일에 대한 STEP의 걸음을 확인한다 ( 년 월 일에 있는 스탭을 가져 왔다 ~ PedTBL 으로부터)
        Cursor curDB = db.rawQuery("SELECT step FROM PedTBL WHERE year="+year+" AND month="+month+" AND day="+day+";",null);
        while (curDB.moveToNext()){
            list.add(curDB.getString(0));
        }
        return list;
    }

    // X축 년 월 일 구하기
    public ArrayList<String>getCalendar(PedColumnVO pedColumnVO) {

        SQLiteDatabase db = getWritableDatabase();
        String step = pedColumnVO.getStep();
        String kcal = pedColumnVO.getKcal();

        // 걸음 , 칼로리에 대한 년 월 일을 확인한다 . ( 걸음 칼로리에 있는 년 월 일 을 가져 온다
        Cursor curCalendar = db.rawQuery("SELECT year ,month, day FROM PedTBL WHERE step=" + step + " AND kcal=" + kcal + ";", null);
        while (curCalendar.moveToNext()) {
            listData.add(curCalendar.getString(0));
        }
        return listData;
    }
    //PedTBL 만보기

    public long addAlarm() {
        Log.i(getClass().getSimpleName(), "addAlarm()...");
        return addAlarm(new AlarmVO());
    }

    long addAlarm(AlarmVO alarm) {
        Log.i(getClass().getSimpleName(), "addAlarm(Alarm alarm) ...");
        return getWritableDatabase().insert("alarmTBL", null, AlarmUtils.toContentValues(alarm));
    }

    public int updateAlarm(AlarmVO alarm) {
        final String where = _ID + "=?";
        final String[] whereArgs = new String[] { Long.toString(alarm.getId()) };
        Log.i(getClass().getSimpleName(), "updateAlarm...");
        return getWritableDatabase()
                .update("alarmTBL", AlarmUtils.toContentValues(alarm), where, whereArgs);
    }

    public int deleteAlarm(AlarmVO alarm) {
        return deleteAlarm(alarm.getId());
    }

    int deleteAlarm(long id) {
        final String where = _ID + "=?";
        final String[] whereArgs = new String[] { Long.toString(id) };
        return getWritableDatabase().delete("alarmTBL", where, whereArgs);
    }

    public ArrayList<AlarmVO> getAlarms() {
        Log.i(getClass().getSimpleName(), "getAlarms()...");
        Cursor c = null;

        try{
            c = getReadableDatabase().query("alarmTBL", null, null, null, null, null, null);
            return AlarmUtils.buildAlarmList(c);
        } finally {
            if (c != null && !c.isClosed()) c.close();
        }

    }
}
