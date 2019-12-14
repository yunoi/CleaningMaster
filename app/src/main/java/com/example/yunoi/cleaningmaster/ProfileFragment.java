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
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment implements View.OnClickListener {
    TextView txtNickName, txtScore, txtRank, txtHeight, txtWeight, txtGender, txtAge;
//    Button btnEditProfile, btnDeleteProfile;
    View view;

    SQLiteDatabase sqLiteDatabase;
    Cursor cursor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile, container, false);
        customActionBar(inflater);

        txtNickName = view.findViewById(R.id.txtNickName);
        txtScore = view.findViewById(R.id.txtScore);
        txtRank = view.findViewById(R.id.txtRank);
        txtHeight = view.findViewById(R.id.txtHeight);
        txtWeight = view.findViewById(R.id.txtWeight);
        txtGender = view.findViewById(R.id.txtGender);
        txtAge = view.findViewById(R.id.txtAge);
//        btnEditProfile = view.findViewById(R.id.btnEditProfile);
//        btnDeleteProfile = view.findViewById(R.id.btnDeleteProfile);

        sqLiteDatabase = DBHelper.getInstance(getActivity().getApplicationContext()).getWritableDatabase();
        cursor = sqLiteDatabase.rawQuery("SELECT * FROM profileTBL;", null);
        cursor.moveToLast();
        String cursorNickName = cursor.getString(cursor.getColumnIndex("NickName"));
        int cursorScore = cursor.getInt(cursor.getColumnIndex("Score"));
        String cursorRank = cursor.getString(cursor.getColumnIndex("Rank"));
        String cursorGender = cursor.getString(cursor.getColumnIndex("Gender"));
        int cursorHeight = cursor.getInt(cursor.getColumnIndex("Height"));
        int cursorWeight = cursor.getInt(cursor.getColumnIndex("Weight"));
        int cursorAge = cursor.getInt(cursor.getColumnIndex("Age"));

        txtNickName.setText(cursorNickName);
        txtScore.setText(String.valueOf(cursorScore));
        txtRank.setText(String.valueOf(cursorRank));
        txtGender.setText(cursorGender);
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
        ImageButton acbar_delete = actionbarlayout.findViewById(R.id.acbar_delete);
        ImageButton acbar_edit = actionbarlayout.findViewById(R.id.acbar_edit);

        acbar_backToMain.setOnClickListener(this);
        acbar_delete.setOnClickListener(this);
        acbar_edit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.acbar_backToMain:
                Fragment mainFragment = new MainFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.coordinatorLayout, mainFragment).commit();

//                fragmentTransaction.remove(ProfileFragment.this).commit();
//                fragmentManager.popBackStack();
                break;
            case R.id.acbar_delete:
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
                    }
                });
                deleteCheck.setNegativeButton("취소",null);
                deleteCheck.show();
                break;
            case R.id.acbar_edit:
                String cursorData = cursor.getString(cursor.getColumnIndex("NickName"));
                Intent intent = new Intent(getContext(), ProfileFirstSetting.class);
                intent.putExtra("nickName", cursorData);
                startActivity(intent);
                break;
        }
    }
}
