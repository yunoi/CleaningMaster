package com.example.yunoi.cleaningmaster;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class ProfileFragment extends Fragment {
    TextView txtNickName,txtScore,txtRank,txtHeight,txtWeight,txtGender,txtAge;
    Button btnEditProfile,btnDeleteProfile;
    View view;

    SQLiteDatabase sqLiteDatabase;
    Cursor cursor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile,container,false);
        txtNickName = view.findViewById(R.id.txtNickName);
        txtScore = view.findViewById(R.id.txtScore);
        txtRank = view.findViewById(R.id.txtRank);
        txtHeight = view.findViewById(R.id.txtHeight);
        txtWeight = view.findViewById(R.id.txtWeight);
        txtGender = view.findViewById(R.id.txtGender);
        txtAge = view.findViewById(R.id.txtAge);
        btnEditProfile = view.findViewById(R.id.btnEditProfile);
        btnDeleteProfile = view.findViewById(R.id.btnDeleteProfile);

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
}
