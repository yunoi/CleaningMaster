package com.example.yunoi.cleaningmaster;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import static android.content.Context.MODE_PRIVATE;

public class SettingFragment extends Fragment implements View.OnClickListener {

    private View view;
    private ImageButton setting_goProfile,setting_pfofileDelete,lisenceBtn;
    private Switch swNotify,swTutoCheck;


    private DBHelper dbHelper;
    private SQLiteDatabase sqLiteDatabase;
    Cursor cursor;
    String cursorData;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.setting_fragment, container, false);
//        swNotify = view.findViewById(R.id.swNotify);
        swTutoCheck = view.findViewById(R.id.swTutoCheck);
        setting_goProfile = view.findViewById(R.id.setting_goProfile);
        setting_pfofileDelete = view.findViewById(R.id.setting_pfofileDelete);
        lisenceBtn = view.findViewById(R.id.lisenceBtn);

        //액션바 설정
        ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        // Custom Actionbar를 사용하기 위해 CustomEnabled을 true 시키고 필요 없는 것은 false 시킨다
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);            //액션바 아이콘을 업 네비게이션 형태로 표시합니다.
        actionBar.setDisplayShowTitleEnabled(false);        //액션바에 표시되는 제목의 표시유무를 설정합니다.
        actionBar.setDisplayShowHomeEnabled(false);//홈 아이콘을 숨김처리합니다.

        View actionbarlayout = inflater.inflate(R.layout.setting_actionbar_layout, null);
        actionBar.setCustomView(actionbarlayout);
        //액션바 양쪽 공백 없애기
        Toolbar parent = (Toolbar) actionbarlayout.getParent();
        parent.setContentInsetsAbsolute(0, 0);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(actionbarlayout, params);

        //end of 액션바 공백 없애기

        //액션바 버튼
        ImageButton imageButton = actionbarlayout.findViewById(R.id.actionbar_settingBtnBack);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new MainFragment(); //돌아가는 fragment
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.coordinatorLayout, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        //////////////////////////////////////////////////여기까지 by채현

        //재훈's part
        //튜토리얼 관련 SharedPreferences
        final SharedPreferences passTutorial = getActivity().getSharedPreferences("change",MODE_PRIVATE);
        int tutorialState = passTutorial.getInt("First",0);
        if(tutorialState==1){
            swTutoCheck.setChecked(false);
        }else if (tutorialState==0){
            swTutoCheck.setChecked(true);
        }

        swTutoCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(swTutoCheck.isChecked()){
                    int intoTuto = 0;
                    SharedPreferences.Editor editor = passTutorial.edit();
                    editor.putInt("First",intoTuto);
                    editor.commit();
                    Toast.makeText(getActivity().getApplicationContext(),"도움말 보기 설정",Toast.LENGTH_SHORT).show();
                }else {
                    int intoMain = 1;
                    SharedPreferences.Editor editor = passTutorial.edit();
                    editor.putInt("First",intoMain);
                    editor.commit();
                    Toast.makeText(getActivity().getApplicationContext(),"도움말 보기 해제",Toast.LENGTH_SHORT).show();
                }
            }
        });

        setting_goProfile.setOnClickListener(this);
        setting_pfofileDelete.setOnClickListener(this);
        lisenceBtn.setOnClickListener(this);
        return view;
    }//end of onCreateView

    @Override
    public void onClick(View v) {
        sqLiteDatabase = DBHelper.getInstance(getActivity().getApplicationContext()).getWritableDatabase();
        cursor = sqLiteDatabase.rawQuery("SELECT * FROM profileTBL;", null);
        cursor.moveToLast();
        String cursorData = cursor.getString(cursor.getColumnIndex("NickName"));
        switch (v.getId()){
            case R.id.setting_goProfile:
//                String cursorData = cursor.getString(cursor.getColumnIndex("NickName"));
                Intent intent = new Intent(getContext(), ProfileFirstSetting.class);
                intent.putExtra("nickName", cursorData);
                startActivity(intent);
                getActivity().finish();
                break;
            case R.id.setting_pfofileDelete:
                AlertDialog.Builder deleteCheck = new AlertDialog.Builder(getContext());
                deleteCheck.setTitle("프로필 삭제");
                deleteCheck.setIcon(R.drawable.warnning);
                deleteCheck.setMessage("정말로 삭제하시겠습니까?");
                deleteCheck.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sqLiteDatabase.execSQL("DELETE FROM profileTBL;");
                        SharedPreferences passTutorial = getActivity().getSharedPreferences("change",MODE_PRIVATE);
                        SharedPreferences.Editor editor = passTutorial.edit();
                        int intoTuto = 0;
                        editor.putInt("First",intoTuto);
                        editor.commit();
                        Intent resetIntent = new Intent(getContext(), NickNameSetting.class);
                        startActivity(resetIntent);
                        getActivity().finish();
                    }
                });
                deleteCheck.setNegativeButton("취소",null);
                deleteCheck.show();
                break;
            case R.id.lisenceBtn:
                Intent lisenceActivity=new Intent(getContext(),LisenceActivity.class);
                startActivity(lisenceActivity);

                break;
        }
    }
}
