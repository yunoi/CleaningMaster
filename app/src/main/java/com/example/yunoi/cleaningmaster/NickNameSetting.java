package com.example.yunoi.cleaningmaster;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.stetho.Stetho;

public class NickNameSetting extends Activity {
    EditText edtNickName;
    Button btnNickSave;

    private DBHelper dbHelper;
    private SQLiteDatabase sqLiteDatabase;
    Cursor cursor;
    String cursorData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Stetho.initializeWithDefaults(this);
        setContentView(R.layout.nickname_setting);

        edtNickName = findViewById(R.id.edtNickName);
        btnNickSave = findViewById(R.id.btnNickSave);


        sqLiteDatabase = DBHelper.getInstance(getApplicationContext()).getWritableDatabase();

        cursor = sqLiteDatabase.rawQuery("SELECT * FROM profileTBL;", null);
        if (cursor.getCount() != 0) {
            cursor.moveToLast();
            cursorData = cursor.getString(cursor.getColumnIndex("NickName"));
            Log.d("problem",cursorData);
        }else {
            cursor.moveToFirst();
        }
        if (cursorData == null) {
            btnNickSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (edtNickName.getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(), "닉네임을 설정해주세요.", Toast.LENGTH_SHORT).show();
                    } else {
                        //DB에 insert
                        sqLiteDatabase = DBHelper.getInstance(getApplicationContext()).getWritableDatabase();
                        sqLiteDatabase.execSQL("INSERT INTO profileTBL VALUES('" + edtNickName.getText().toString() + "',0,null,null,0,0,0);");
                        cursor = sqLiteDatabase.rawQuery("SELECT * FROM profileTBL;", null);
                        cursor.moveToLast();
                        cursorData = cursor.getString(cursor.getColumnIndex("NickName"));
                        //메세지 소환하며 프로필 설정으로 넘어가기
                        Toast.makeText(NickNameSetting.this, cursorData + "님, 환영합니다.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(NickNameSetting.this, ProfileFirstSetting.class);
                        intent.putExtra("nickName", cursorData);
                        startActivity(intent);
                        finish();

                    }
                }
            });
        } else if (cursorData != null) {
            Toast.makeText(NickNameSetting.this, cursorData + "님, 환영합니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(NickNameSetting.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        cursor.close();
    }
}
