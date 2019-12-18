package com.example.yunoi.cleaningmaster;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.facebook.stetho.Stetho;

public class ProfileFirstSetting extends Activity implements View.OnClickListener {
    Button btnProfileSave, btnPass;
    ImageButton ibMale,ibFemale;
    EditText edtHeight, edtWeight, edtAge;

    private DBHelper dbHelper;
    private SQLiteDatabase sqLiteDatabase;
    Cursor cursor;
    String cursorData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Stetho.initializeWithDefaults(this);
        setContentView(R.layout.profile_setting);
        //id연결
        ibMale = findViewById(R.id.ibMale);
        ibFemale = findViewById(R.id.ibFemale);
        btnProfileSave = findViewById(R.id.btnProfileSave);
        edtHeight = findViewById(R.id.edtHeight);
        edtWeight = findViewById(R.id.edtWeight);
        edtAge = findViewById(R.id.edtAge);
        btnPass = findViewById(R.id.btnPass);
        //EditText 숫자 제한
        edtHeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String cost = s.toString().trim();

                if(!cost.endsWith(".") && cost.contains(".")){
                    String numberBeforeDecimal = cost.split("\\.")[0];
                    String numberAfterDecimal = cost.split("\\.")[1];

                    if(numberAfterDecimal.length() > 2){
                        numberAfterDecimal = numberAfterDecimal.substring(0, 2);
                    }
                    cost = numberBeforeDecimal + "." + numberAfterDecimal;
                }
                edtHeight.removeTextChangedListener(this);
                edtHeight.setText(cost);
                edtHeight.setSelection(edtHeight.getText().toString().trim().length());
                edtHeight.addTextChangedListener(this);
            }
        });
        //EditText 숫자 제한
        edtWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String cost = s.toString().trim();

                if(!cost.endsWith(".") && cost.contains(".")){
                    String numberBeforeDecimal = cost.split("\\.")[0];
                    String numberAfterDecimal = cost.split("\\.")[1];

                    if(numberAfterDecimal.length() > 2){
                        numberAfterDecimal = numberAfterDecimal.substring(0, 2);
                    }
                    cost = numberBeforeDecimal + "." + numberAfterDecimal;
                }
                edtWeight.removeTextChangedListener(this);
                edtWeight.setText(cost);
                edtWeight.setSelection(edtWeight.getText().toString().trim().length());
                edtWeight.addTextChangedListener(this);
            }
        });
        //profileTBL항목에서 성별에 따라서 버튼의 색상을 미리 결정해둔다.
        sqLiteDatabase = DBHelper.getInstance(getApplicationContext()).getWritableDatabase();
        Intent intent = getIntent();
        String data = intent.getStringExtra("nickName");
        cursor = sqLiteDatabase.rawQuery("SELECT * FROM profileTBL WHERE NickName = '" + data + "';", null);
        if (cursor.getCount() != 0) {
            cursor.moveToLast();
        }
        cursorData = cursor.getString(cursor.getColumnIndex("NickName"));
        String cursorGender = cursor.getString(cursor.getColumnIndex("Gender"));
        if(cursorGender.equals("여성")){
            ibFemale.setBackgroundColor(Color.LTGRAY);
            ibMale.setEnabled(false);
        }else if (cursorGender.equals("남성")){
            ibMale.setBackgroundColor(Color.LTGRAY);
            ibFemale.setEnabled(false);
        }

        ibFemale.setOnClickListener(this);
        ibMale.setOnClickListener(this);
        btnProfileSave.setOnClickListener(this);
        btnPass.setOnClickListener(this);

    }//end of onCre

    @Override
    public void onClick(View v) {
        //DB를 불러오고 이후 인텐트로 받아온 닉네임을 DB의 profileTBL의 닉네임과 같은 항목의 수정을 진행한다.
        sqLiteDatabase = DBHelper.getInstance(getApplicationContext()).getWritableDatabase();
        Intent intent = getIntent();
        String data = intent.getStringExtra("nickName");
        cursor = sqLiteDatabase.rawQuery("SELECT * FROM profileTBL WHERE NickName = '" + data + "';", null);
        if (cursor.getCount() != 0) {
            cursor.moveToLast();
        }
        cursorData = cursor.getString(cursor.getColumnIndex("NickName"));
        String cursorGender = cursor.getString(cursor.getColumnIndex("Gender"));
        switch (v.getId()){
            //여성 이미지 버튼 클릭이벤트
            case R.id.ibFemale :
                if(cursorGender.equals("성별")){
                    sqLiteDatabase.execSQL("UPDATE profileTBL SET Gender = '" + "여성" + "';");
                    ibFemale.setBackgroundColor(Color.LTGRAY);
                    ibMale.setEnabled(false);
                }else {
                    sqLiteDatabase.execSQL("UPDATE profileTBL SET Gender = '" + "성별" + "';");
                    ibFemale.setBackgroundColor(Color.TRANSPARENT);
                    ibMale.setEnabled(true);
                }
                break;
            //남성 이미지 버튼 클릭이벤트
            case R.id.ibMale :
                if(cursorGender.equals("성별")){
                    sqLiteDatabase.execSQL("UPDATE profileTBL SET Gender = '" + "남성" + "';");
                    ibMale.setBackgroundColor(Color.LTGRAY);
                    ibFemale.setEnabled(false);
                }else {
                    sqLiteDatabase.execSQL("UPDATE profileTBL SET Gender = '" + "성별" + "';");
                    ibMale.setBackgroundColor(Color.TRANSPARENT);
                    ibFemale.setEnabled(true);
                }

                break;
            //프로필 저장 버튼 클릭이벤트
            case R.id.btnProfileSave :
                if(edtHeight.getText().toString().equals("")||edtWeight.getText().toString().equals("")||
                        edtAge.getText().toString().equals("")){
                    Toast.makeText(this,"공백이 있습니다. 입력하세요",Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    float height = Float.parseFloat(edtHeight.getText().toString());
                    float weight = Float.parseFloat(edtWeight.getText().toString());
                    int age = Integer.parseInt(edtAge.getText().toString());
                    sqLiteDatabase.execSQL("UPDATE profileTBL SET Height = " + height + ", Weight = " + weight + ", Age = " +
                            age + " WHERE NickName = '" + cursorData + "';");
                }
                EndPage();
                cursor.close();
                break;
            //다음에 저장 버튼 클릭이벤트
            case R.id.btnPass :
                EndPage();
                break;
        }
    }
    //메인으로 이동
    private void EndPage() {
        Intent intent = new Intent(ProfileFirstSetting.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
