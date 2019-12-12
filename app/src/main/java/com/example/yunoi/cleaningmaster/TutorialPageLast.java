package com.example.yunoi.cleaningmaster;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;

public class TutorialPageLast extends Fragment {
    ImageButton btnTutoClose;
    CheckBox cbEndTuto;
    View view;
    public static TutorialPageLast newInstance(){
        TutorialPageLast tutorialPageLast=new TutorialPageLast();
        return tutorialPageLast;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tutorial_page_last,container,false);
        cbEndTuto=view.findViewById(R.id.cbEndTuto);
        btnTutoClose=view.findViewById(R.id.btnTutoClose);
        btnTutoClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if(cbEndTuto.isChecked()){
                    int intoMain = 1;
                    SharedPreferences.Editor editor = TutorialGuideActivity.sharedPreferences.edit();
                    editor.putInt("First",intoMain);
                    editor.commit();
                }
                getActivity().finish();
            }
        });
        return view;
    }
}
