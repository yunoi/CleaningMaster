package com.example.yunoi.cleaningmaster;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class InitActionReceiver extends BroadcastReceiver {

    private SQLiteDatabase db;

    public InitActionReceiver() {
        super();
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();

        if (Intent.ACTION_DATE_CHANGED.equals(action)) {
            // 날짜가 변경된 경우 해야 될 작업을 한다.

            updateIsCheckTBL(context);
            insertCheck(context);

            Log.d("확인","초기화");

        }

    }
    //저장된 ISCHECKTBL
    public void updateIsCheckTBL(Context context){
        db = DBHelper.getInstance(context).getWritableDatabase();
        db.execSQL("UPDATE ischeckTBL SET isCheckClear=0 ;");
    }
    // 체크 정보 업데이트 (0_false, 1_true)
    public void insertCheck(Context context) {
        db = DBHelper.getInstance(context).getWritableDatabase();
        db.execSQL("UPDATE cleaningTBL SET checkCount=0 ;");
    }




}
