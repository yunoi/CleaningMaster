package com.example.yunoi.cleaningmaster;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class OpeningActivity extends Activity {
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //2초 동안 해당 오프닝 화면부터 시작
        setContentView(R.layout.opening);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(),NickNameSetting.class);
                startActivity(intent);
                finish();
            }
        },2000);
    }
}
