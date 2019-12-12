package com.example.yunoi.cleaningmaster;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TutorialPage3 extends Fragment {
    View view;
    public static TutorialPage3 newInstance(){
        TutorialPage3 tutorialPage3=new TutorialPage3();
        return tutorialPage3;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tutorial_page_3,container,false);
        return view;
    }
}
