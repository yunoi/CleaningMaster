package com.example.yunoi.cleaningmaster;

import android.app.Activity;
import android.os.Bundle;

public class PedomterNotiControlView extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String foo = (String)getIntent().getExtras().get("Foo");
        if(foo.equals("bar")){

        }
    }
}

