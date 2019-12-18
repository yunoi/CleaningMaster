package com.example.yunoi.cleaningmaster;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dinuscxj.progressbar.CircleProgressBar;

public class ProfileFragment extends Fragment implements View.OnClickListener {
    TextView txtNickName, txtScore, txtRank, txtHeight, txtWeight, txtAge;
    ImageView ivGender, ivMedal;
    View view;
    CircleProgressBar profile_progressBar;
    ProgressBarAnimation progressBarAnimation;

    SQLiteDatabase sqLiteDatabase;
    Cursor cursor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile, container, false);
        customActionBar(inflater);
        //ID연결
        txtNickName = view.findViewById(R.id.txtNickName);
        txtScore = view.findViewById(R.id.txtScore);
        profile_progressBar = view.findViewById(R.id.profile_progressBar);
        txtRank = view.findViewById(R.id.txtRank);
        ivMedal = view.findViewById(R.id.ivMedal);
        txtHeight = view.findViewById(R.id.txtHeight);
        txtWeight = view.findViewById(R.id.txtWeight);
        ivGender = view.findViewById(R.id.ivGender);
        txtAge = view.findViewById(R.id.txtAge);

        //profileTBL항목에서 자료들을 불러와 각 항목에 미리 세팅시킨다.
        sqLiteDatabase = DBHelper.getInstance(getActivity().getApplicationContext()).getWritableDatabase();
        cursor = sqLiteDatabase.rawQuery("SELECT * FROM profileTBL;", null);
        cursor.moveToLast();
        String cursorNickName = cursor.getString(cursor.getColumnIndex("NickName"));
        int cursorScore = cursor.getInt(cursor.getColumnIndex("Score"));
        String cursorRank = cursor.getString(cursor.getColumnIndex("Rank"));
        String cursorGender = cursor.getString(cursor.getColumnIndex("Gender"));
        float cursorHeight = cursor.getFloat(cursor.getColumnIndex("Height"));
        float cursorWeight = cursor.getFloat(cursor.getColumnIndex("Weight"));
        int cursorAge = cursor.getInt(cursor.getColumnIndex("Age"));

        //프로그래스바 관련 코드
        //프로그래스바 세팅
        progressBarAnimation = new ProgressBarAnimation(profile_progressBar, 0, cursorScore);
        progressBarAnimation.setDuration(1000);
        //프로그래스바 모양
        profile_progressBar.setCap(Paint.Cap.ROUND);
        profile_progressBar.setMax(1000);
        profile_progressBar.startAnimation(progressBarAnimation);

        //프로필 항목들에 DB에서 불러온 자료 삽입
        txtNickName.setText(cursorNickName);
        txtScore.setText(String.valueOf(cursorScore));
        txtRank.setText(String.valueOf(cursorRank));
        switch (txtRank.getText().toString()){
            case "초수": ivMedal.setImageResource(R.drawable.medal);break;
            case "중수": ivMedal.setImageResource(R.drawable.winner);break;
            case "고수": ivMedal.setImageResource(R.drawable.award);break;
            case "마스터": ivMedal.setImageResource(R.drawable.master);break;
        }
        if (cursorGender.equals("남성")) {
            ivGender.setImageResource(R.drawable.man_96);
        } else if (cursorGender.equals("여성")) {
            ivGender.setImageResource(R.drawable.woman_96);
        } else {
            ivGender.setImageResource(R.drawable.gender_96);
        }
        txtHeight.setText(String.valueOf(cursorHeight));
        txtWeight.setText(String.valueOf(cursorWeight));
        txtAge.setText(String.valueOf(cursorAge));

        return view;
    }

    private void customActionBar(LayoutInflater inflater) {

        ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();

        // Custom Actionbar 사용하기 위해 CustomEnabled을 true 시키고 필요 없는 것은 false 시킨다
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);         //액션바 아이콘을 업 네비게이션 형태로 표시합니다.
        actionBar.setDisplayShowTitleEnabled(false);        //액션바에 표시되는 제목의 표시유무를 설정합니다.
        actionBar.setDisplayShowHomeEnabled(false);         //홈 아이콘을 숨김처리합니다.

        View actionbarlayout = inflater.inflate(R.layout.profile_actionbar_layout, null);
        actionBar.setCustomView(actionbarlayout);
        //액션바 양쪽 공백 없애기
        Toolbar parent = (Toolbar) actionbarlayout.getParent();
        parent.setContentInsetsAbsolute(0, 0);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);

        actionBar.setCustomView(actionbarlayout, params);

        ////end of 액션바 공백 없애기

        ImageButton acbar_backToMain = actionbarlayout.findViewById(R.id.acbar_backToMain);

        acbar_backToMain.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.acbar_backToMain:
                Fragment mainFragment = new MainFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.coordinatorLayout, mainFragment).commit();

                break;
        }

    }

    //프로그래스바 애니메이션
    public class ProgressBarAnimation extends Animation {
        CircleProgressBar circleProgressBar;
        private float from;
        private float to;

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
}


