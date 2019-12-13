package com.example.yunoi.cleaningmaster;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.dinuscxj.progressbar.CircleProgressBar;
import com.sackcentury.shinebuttonlib.ShineButton;

public class ExpShowActivity extends AppCompatActivity implements View.OnClickListener {


    ImageButton todo_btnCancel;
    CircleProgressBar todo_progessBar;
    TextView todo_txtCore;
    TextView todo_txtLevel;
    ShineButton todo_shineButton;
    ImageView todo_medal;
    ImageView todo_celebration;
    ProgressBarAnimation progressBarAnimation;;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expshow_layout);
        getSupportActionBar().hide();

        todo_btnCancel=findViewById(R.id.todo_btnCancel);
        todo_progessBar=findViewById(R.id.todo_progessBar);
        todo_txtCore=findViewById(R.id.todo_txtCore);
        todo_txtLevel=findViewById(R.id.todo_txtLevel);
        todo_shineButton=findViewById(R.id.todo_shineButton);
        todo_medal=findViewById(R.id.todo_medal);
        todo_celebration=findViewById(R.id.todo_celebration);

        int score=TodolistAdapter.score;
        progressBarAnimation=new ProgressBarAnimation(todo_progessBar,0, score);
        progressBarAnimation.setDuration(1000);
        todo_txtCore.setText(String.valueOf(score));
        todo_progessBar.startAnimation(progressBarAnimation);


        if (score>=1000 && score < 2000){



        }







        todo_btnCancel.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.todo_btnCancel: finish(); break;

        }
    }

    public class ProgressBarAnimation extends Animation {
        CircleProgressBar circleProgressBar;
        private float from;
        private float  to;

        public ProgressBarAnimation(CircleProgressBar circleProgressBar, float from, float to) {
            super();
            this.circleProgressBar = circleProgressBar;
            this.from = from;
            this.to = to;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            float value = from + (to - from) * interpolatedTime;
            circleProgressBar.setProgress((int) value);
        }

    }
}
