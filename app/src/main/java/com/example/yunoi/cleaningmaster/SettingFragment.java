package com.example.yunoi.cleaningmaster;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import static android.content.Context.MODE_PRIVATE;

public class SettingFragment extends Fragment {

    private View view;
    private Switch swNotify,swTutoCheck;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.setting_fragment, container, false);
        swNotify = view.findViewById(R.id.swNotify);
        swTutoCheck = view.findViewById(R.id.swTutoCheck);

        //
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
        return view;
    }
}
