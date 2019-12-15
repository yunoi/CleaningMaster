package com.example.yunoi.cleaningmaster;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.facebook.stetho.Stetho;

public class ProfileFirstSetting extends Activity implements View.OnClickListener {
    Button btnMale, btnFemale, btnProfileSave, btnPass;
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

        btnMale = findViewById(R.id.btnMale);
        btnFemale = findViewById(R.id.btnFemale);
        btnProfileSave = findViewById(R.id.btnProfileSave);
        edtHeight = findViewById(R.id.edtHeight);
        edtWeight = findViewById(R.id.edtWeight);
        edtAge = findViewById(R.id.edtAge);
        btnPass = findViewById(R.id.btnPass);

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

        btnFemale.setOnClickListener(this);
        btnMale.setOnClickListener(this);
        btnProfileSave.setOnClickListener(this);
        btnPass.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        sqLiteDatabase = DBHelper.getInstance(getApplicationContext()).getWritableDatabase();
        switch (v.getId()){
            case R.id.btnFemale :
                sqLiteDatabase.execSQL("UPDATE profileTBL SET Gender = '" + "여성" + "';");
                break;
            case R.id.btnMale :
                sqLiteDatabase.execSQL("UPDATE profileTBL SET Gender = '" + "남성" + "';");
                break;
            case R.id.btnProfileSave :
                Intent intent = getIntent();
                String data = intent.getStringExtra("nickName");

                float height = Float.parseFloat(edtHeight.getText().toString());
                float weight = Float.parseFloat(edtWeight.getText().toString());
                int age = Integer.parseInt(edtAge.getText().toString());
                cursor = sqLiteDatabase.rawQuery("SELECT * FROM profileTBL WHERE NickName = '" + data + "';", null);
                if (cursor.getCount() != 0) {
                    cursor.moveToLast();
                }
                cursorData = cursor.getString(cursor.getColumnIndex("NickName"));
                sqLiteDatabase.execSQL("UPDATE profileTBL SET Height = " + height + ", Weight = " + weight + ", Age = " +
                        age + " WHERE NickName = '" + cursorData + "';");
                cursor.close();
                EndPage();
                break;
            case R.id.btnPass :
                EndPage();
                break;
        }
    }
    private void EndPage() {
        Intent intent = new Intent(ProfileFirstSetting.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
