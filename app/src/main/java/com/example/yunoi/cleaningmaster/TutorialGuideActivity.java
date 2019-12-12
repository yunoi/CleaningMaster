package com.example.yunoi.cleaningmaster;

import android.content.SharedPreferences;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class TutorialGuideActivity extends AppCompatActivity {
    ViewPager vpTutorial;

    FragmentPagerAdapter fragmentPagerAdapter;

    public static SharedPreferences spPassTutorial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_guide);
        setTitle("Tutorial");

        vpTutorial = findViewById(R.id.vpTutorial);

        fragmentPagerAdapter = new TutorialVierPagerAdapter(getSupportFragmentManager());
        vpTutorial.setAdapter(fragmentPagerAdapter);

        spPassTutorial = getSharedPreferences("change",MODE_PRIVATE);

    }
}
