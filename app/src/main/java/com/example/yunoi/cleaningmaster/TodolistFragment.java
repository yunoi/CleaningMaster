package com.example.yunoi.cleaningmaster;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class TodolistFragment extends Fragment {

    View view;

    ArrayList<TodolistVo> list=new ArrayList<TodolistVo>();
    TodolistAdapter todolistAdapter;
    LinearLayoutManager linearLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.todolist_fragment,container,false);
        RecyclerView recyclerView =view.findViewById(R.id.todo_listView);

        //액션바 설정
        ActionBar actionBar=((MainActivity)getActivity()).getSupportActionBar();
        // Custom Actionbar를 사용하기 위해 CustomEnabled을 true 시키고 필요 없는 것은 false 시킨다
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);            //액션바 아이콘을 업 네비게이션 형태로 표시합니다.
        actionBar.setDisplayShowTitleEnabled(false);        //액션바에 표시되는 제목의 표시유무를 설정합니다.
        actionBar.setDisplayShowHomeEnabled(false);//홈 아이콘을 숨김처리합니다.

        View actionbarlayout=inflater.inflate(R.layout.todolist_actionbar_layout,null);
        actionBar.setCustomView(actionbarlayout);
        //액션바 양쪽 공백 없애기
        Toolbar parent = (Toolbar)actionbarlayout.getParent();
        parent.setContentInsetsAbsolute(0,0);
        ActionBar.LayoutParams params=new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(actionbarlayout,params);

        //end of 액션바 공백 없애기

        //액션바 버튼
        ImageButton imageButton =actionbarlayout.findViewById(R.id.actionbar_todoBtnBack);
        TextView  actionbar_todoText=actionbarlayout.findViewById(R.id.actionbar_todoText);
        ImageButton actionbar_todoBtnAddlist =actionbarlayout.findViewById(R.id.actionbar_todoBtnAddlist);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment=new MainFragment(); //돌아가는 fragment
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.coordinatorLayout,fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        //mainFragment 에서 번들값 받아옴.
        if (getArguments()!=null){
            String groupText=getArguments().getString("groupText");
            actionbar_todoText.setText(groupText);
        }

        //리싸이클러뷰 설정
        linearLayoutManager=new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        todolistAdapter=new TodolistAdapter(R.layout.todo_list_holder_layout,list);
        recyclerView.setAdapter(todolistAdapter);

        //list 추가 버튼 -> 알런트 창 -> 확인 -> list 추가

        actionbar_todoBtnAddlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final View alertDialogView=View.inflate(v.getContext(),R.layout.dialog_add_todolist,null);
                AlertDialog.Builder builder=new AlertDialog.Builder(v.getContext(),R.style.MyDialogTheme);
                builder.setView(alertDialogView);
                builder.setPositiveButton("저장", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText alerEdt=alertDialogView.findViewById(R.id.alerEdt);
                        list.add(new TodolistVo(alerEdt.getText().toString().trim()));
                        todolistAdapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("취소",null);
                builder.show();


            }
        });


        return view;
    }
}
