package com.example.yunoi.cleaningmaster;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TodolistFragment extends Fragment {

    View view;
    private SQLiteDatabase db;

    private ArrayList<TodolistVo> list=new ArrayList<TodolistVo>();
    private TodolistAdapter todolistAdapter;
    private LinearLayoutManager linearLayoutManager;

    private String groupText; //구역이름
    public static int taskcount=0; //그 구역의 총 리스트 사이즈
    private static final String TAG="확인";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.todolist_fragment,container,false);
        RecyclerView recyclerView =view.findViewById(R.id.todo_listView);
//
//        //현재 년,월,일
//        Calendar calendar=Calendar.getInstance();
//        Date date=calendar.getTime();
//        String year=new SimpleDateFormat("YYYY").format(date);
//        String month=new SimpleDateFormat("MM").format(date);
//        String day=new SimpleDateFormat("dd").format(date);
//
//        final int currentYear=Integer.parseInt(year);
//        final int currentMonth=Integer.parseInt(month);
//        final int currentDay=Integer.parseInt(day);
//
//
//        Log.d(TAG,"날짜 : "+currentYear+"년"+currentMonth+"월"+currentDay+"일");

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
            groupText=getArguments().getString("groupText");
            actionbar_todoText.setText(groupText);
        }



        //리싸이클러뷰 설정
        linearLayoutManager=new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        todolistAdapter=new TodolistAdapter(R.layout.todo_list_holder_layout,list);
        recyclerView.setAdapter(todolistAdapter);

        selectCleaningArea(groupText);

        //list 추가 버튼 -> 알런트 창 -> 확인 -> list 추가

        actionbar_todoBtnAddlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                final View alertDialogView=View.inflate(v.getContext(),R.layout.dialog_add_todolist,null);
                AlertDialog.Builder builder=new AlertDialog.Builder(v.getContext(),R.style.MyDialogTheme);
                builder.setView(alertDialogView);
                builder.setPositiveButton("저장", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        taskcount++;
                        EditText alerEdt=alertDialogView.findViewById(R.id.alert_todolist_alerEdt);
                        String task=alerEdt.getText().toString();

                        if (task.equals("")){
                            Toast.makeText(v.getContext(),"할 일을 적어주세요!",Toast.LENGTH_SHORT).show();
                        }else {

                            insertCleaningArea(new TodolistVo(0,0,0,groupText,task,taskcount,0));
                            Toast.makeText(v.getContext(),"저장되었습니다!",Toast.LENGTH_SHORT).show();

                            selectCleaningArea(groupText);
                        }


                    }
                });
                builder.setNegativeButton("취소",null);
                builder.show();

            }
        });




        return view;
    }

    //cleaningTBL 구역 저장하기(insert) (현재 년도, 월, 일, 구역, 할일,taskCount 나머지는 2개 checkCount,score 0 으로)
    public void insertCleaningArea(TodolistVo todolistVo) {
        db = DBHelper.getInstance(getActivity().getApplicationContext()).getWritableDatabase();
        int year = todolistVo.getYear();
        int month = todolistVo.getMonth();
        int day = todolistVo.getDay();
        String todolist_text = todolistVo.getTodolist_text();
        String groupName = todolistVo.getGroupName();
        int taskcount = todolistVo.getTaskcount();
        int checkcount = todolistVo.getCheckcount();

        db.execSQL("INSERT INTO cleaningTBL (year, month, day, area, task, taskCount, checkCount)" +
                    "VALUES (" + year + "," + month + "," + day + ",'" + groupName + "','" + todolist_text + "'," + taskcount + ", " + checkcount+");");
        Log.d(TAG,"DB 저장됨");
    }

    //저장된 DB 내용 가져오기 (할일, 할일 카운터)
    public void selectCleaningArea(String name){
        db = DBHelper.getInstance(getActivity().getApplicationContext()).getWritableDatabase();
        Cursor cursor;
        cursor=db.rawQuery("SELECT task,taskcount FROM cleaningTBL WHERE area='"+name+"';",null);
        list.clear();
        while (cursor.moveToNext()){

            list.add(new TodolistVo(cursor.getString(0),cursor.getInt(1)));

        }
        todolistAdapter.notifyDataSetChanged();
        cursor.close();

    }



//db.execSQL("UPDATE cleaningTBL SET area="+"'"+groupName+"',"+"task="+"'"+todolist_text+"'"+" WHERE task=NULL ;");
// db.execSQL("INSERT INTO cleaningTBL (year, month, day, area, task, taskCount, checkCount)" +
//                    "VALUES (" + year + "," + month + "," + day + ",'" + groupName + "','" + todolist_text + "'," + taskcount + ", " + checkcount+");");

//db.execSQL("SELECT task FROM cleaningTBL WHERE area='"+groupName+"';");
}
