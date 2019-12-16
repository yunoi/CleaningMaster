package com.example.yunoi.cleaningmaster;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dinuscxj.progressbar.CircleProgressBar;
import com.sackcentury.shinebuttonlib.ShineButton;

public class ExpShowActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG="확인";
    ImageButton todo_btnCancel;
    CircleProgressBar todo_progessBar;
    TextView todo_txtCore;
    TextView todo_txtLevel;
    ShineButton todo_shineButton;
    ImageView todo_medal;
    ImageView todo_celebration;
    ProgressBarAnimation progressBarAnimation;
    SQLiteDatabase db;
    int score;
    String nickname;


    @Override
    protected void onPause() {
        super.onPause();
        updateScore(getApplicationContext(),score);
        Toast.makeText(getApplicationContext(),"core가 db로 다시 업데이트됨"+ score,Toast.LENGTH_SHORT).show();

    }

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

        todo_progessBar.setCap(Paint.Cap.ROUND);
        todo_progessBar.setMax(1000);

        db = DBHelper.getInstance(getApplicationContext()).getWritableDatabase();
        Cursor cursor;
        cursor = db.rawQuery("SELECT Score,Rank FROM profileTBL ;", null);
        String level=null;
        score=0;
        while (cursor.moveToNext()){
            score=cursor.getInt(0);
            level=cursor.getString(1);
        }
        cursor.close();

        progressBarAnimation=new ProgressBarAnimation(todo_progessBar,0, score);
        progressBarAnimation.setDuration(1000);
        todo_txtCore.setText(String.valueOf(score));
        todo_txtLevel.setText("초수");
        todo_progessBar.startAnimation(progressBarAnimation);


        if (score>=1000 && score < 2000){

            if (level.equals("중수")){
                todo_txtCore.setText(String.valueOf(score));
                todo_txtLevel.setText("중수");
            }else {
                todo_txtCore.setText("레벨업!!!");
                todo_txtLevel.setText("중수");
                selectProfile(getApplicationContext(),"중수");
                todo_shineButton.setVisibility(View.VISIBLE);
                todo_celebration.setVisibility(View.INVISIBLE);
                todo_shineButton.callOnClick();
            }
            progressBarAnimation.to= score -1000;
            todo_progessBar.startAnimation(progressBarAnimation);

        }else if (score >=2000 && score <3000){

            if (level.equals("고수")){
                todo_txtCore.setText(String.valueOf(score));
                todo_txtLevel.setText("고수");
            }else {
                todo_txtCore.setText("레벨업!!!");
                todo_txtLevel.setText("고수");
                selectProfile(getApplicationContext(),"고수");
                todo_shineButton.setVisibility(View.VISIBLE);
                todo_celebration.setVisibility(View.INVISIBLE);
                todo_shineButton.callOnClick();
            }
            progressBarAnimation.to= score -2000;
            todo_progessBar.startAnimation(progressBarAnimation);

        }else if (score >=3000 && score <4000){

            if (level.equals("마스터")){
                todo_txtCore.setText(String.valueOf(score));
                todo_txtLevel.setText("마스터");
            }else {
                todo_txtCore.setText("레벨업!!! 고지가 얼마 안남았어여!!");
                todo_txtLevel.setText("마스터");
                selectProfile(getApplicationContext(),"마스터");
                todo_shineButton.setVisibility(View.VISIBLE);
                todo_celebration.setVisibility(View.INVISIBLE);
                todo_shineButton.callOnClick();
            }
            progressBarAnimation.to= score -3000;
            todo_progessBar.startAnimation(progressBarAnimation);

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



    }//end of onClickLister

    //DB에 저장된 레벨 업데이트
    public void selectProfile(Context context,String level){
        db = DBHelper.getInstance(context).getWritableDatabase();

        Cursor cursor;
        cursor=db.rawQuery("SELECT * FROM profileTBL",null);
        String nickname=null;
        while (cursor.moveToNext()){

            nickname=cursor.getString(0);
        }
        Log.d(TAG,"닉네임 확인 : "+nickname);

        db.execSQL("UPDATE profileTBL SET Rank='"+level+"' WHERE NickName="+"'"+nickname+"'"+";");

        cursor.close();
    }
    //저장된 Score 다시 업데이트
    public void updateScore(Context context,int score){
        db = DBHelper.getInstance(context).getWritableDatabase();
        Cursor cursor;
        cursor=db.rawQuery("SELECT * FROM profileTBL",null);
        String nickname=null;
        while (cursor.moveToNext()){

            nickname=cursor.getString(0);
        }
        Log.d(TAG,"닉네임 확인 : "+nickname);

        db.execSQL("UPDATE profileTBL SET Score='"+score+"' WHERE NickName="+"'"+nickname+"'"+";");
        cursor.close();

    }

}
