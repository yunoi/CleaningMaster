package com.example.yunoi.cleaningmaster;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static android.content.Context.ALARM_SERVICE;


public class TodolistFragment extends Fragment implements View.OnClickListener, TodolistAdapter.OnAlarmCheckedChangeListener {

    View view;
    private SQLiteDatabase db;
    private ArrayList<TodolistVo> list = new ArrayList<TodolistVo>();
    private TodolistAdapter todolistAdapter;
    private LinearLayoutManager linearLayoutManager;
    public static ConstraintLayout todo_constraintLayout;
    public static int score=0;
    public static String groupText; //구역이름
    private static final String TAG = "TodolistFragment";


    // 알림 관련 변수
    private Calendar calendar = Calendar.getInstance(); // 캘린더 인스턴스 생성
    private AlarmManager alarmManager;
    private TimePicker timePicker;
    private TextView tvDate;
    private TextView tvTask;
    private int alarmId = 0;
    private int alarmOnOff = 0;
    private ArrayList<PendingIntent> intentArray = new ArrayList<>();


    // 요일 관련 변수
    private boolean[] btnCheck = new boolean[]{false, false, false, false, false, false, false};
    private final static int NO_LOOP = 0;
    private final static int MON = 1;
    private final static int TUE = 2;
    private final static int WED = 3;
    private final static int THU = 4;
    private final static int FRI = 5;
    private final static int SAT = 6;
    private final static int SUN = 7;
    private int setLoop = 0;
    private TextView txtDayCheck;
    private CheckBox cbMonday;
    private CheckBox cbTuesday;
    private CheckBox cbWednesday;
    private CheckBox cbThursday;
    private CheckBox cbFriday;
    private CheckBox cbSaturday;
    private CheckBox cbSunday;

    private Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.todolist_fragment, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.todo_listView);
        todo_constraintLayout = view.findViewById(R.id.todo_constraintLayout);

        insertLevelInit();

        //액션바 설정
        ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        // Custom Actionbar를 사용하기 위해 CustomEnabled을 true 시키고 필요 없는 것은 false 시킨다
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);            //액션바 아이콘을 업 네비게이션 형태로 표시합니다.
        actionBar.setDisplayShowTitleEnabled(false);        //액션바에 표시되는 제목의 표시유무를 설정합니다.
        actionBar.setDisplayShowHomeEnabled(false);//홈 아이콘을 숨김처리합니다.

        View actionbarlayout = inflater.inflate(R.layout.todolist_actionbar_layout, null);
        actionBar.setCustomView(actionbarlayout);
        //액션바 양쪽 공백 없애기
        Toolbar parent = (Toolbar) actionbarlayout.getParent();
        parent.setContentInsetsAbsolute(0, 0);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(actionbarlayout, params);

        //end of 액션바 공백 없애기

        //액션바 버튼
        ImageButton imageButton = actionbarlayout.findViewById(R.id.actionbar_todoBtnBack);
        TextView actionbar_todoText = actionbarlayout.findViewById(R.id.actionbar_todoText);
        ImageButton actionbar_todoBtnAddlist = actionbarlayout.findViewById(R.id.actionbar_todoBtnAddlist);

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


            if (getArguments() != null) {
                groupText = getArguments().getString("groupText");
                actionbar_todoText.setText(groupText);
            }



            //리싸이클러뷰 설정
            linearLayoutManager = new LinearLayoutManager(view.getContext());
            recyclerView.setLayoutManager(linearLayoutManager);

            todolistAdapter = new TodolistAdapter(R.layout.todo_list_holder_layout, list, context, this);
            recyclerView.setAdapter(todolistAdapter);

            selectCleaningArea(groupText);//구역마다 저장한 할일들 가져오기
            score=selectScore(view.getContext());


        //list 추가 버튼 -> 알런트 창 -> 확인 -> list 추가

        actionbar_todoBtnAddlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                final View alertDialogView = View.inflate(v.getContext(), R.layout.dialog_add_todolist, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext(), R.style.MyDialogTheme);
                builder.setView(alertDialogView);
                builder.setPositiveButton("저장", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        EditText alerEdt = alertDialogView.findViewById(R.id.alert_todolist_alerEdt);
                        String task = alerEdt.getText().toString();
                        if (task.equals("")) {
                            Toast.makeText(v.getContext(), "할 일을 적어주세요!", Toast.LENGTH_SHORT).show();
                        } else {
                            insertCleaningArea(new TodolistVo(0, 0, 0, groupText, task, 0, 0));
                            selectCleaningArea(groupText);
                            todolistAdapter.notifyDataSetChanged();
                            Toast.makeText(v.getContext(), "저장되었습니다!", Toast.LENGTH_SHORT).show();
                        }


                    }
                });
                builder.setNegativeButton("취소", null);
                builder.show();

            }
        });

        // todolist 알람 설정
        todolistAdapter.setAlarmClickListener(new TodolistAdapter.alarmClickListener() {
            @Override
            public void onAlarmClick(View v, int position) {
                alarmSettings(position);
            }
        });
            return view;
        }//end of onCreatView

    ////////////////////////////////////채현이꺼///////////////////////////////////////////////////////////////
        //cleaningTBL 구역 저장하기(insert) (현재 년도, 월, 일, 구역, 할일,taskCount 나머지는 2개 checkCount,score 0 으로)
        public void insertCleaningArea(TodolistVo todolistVo) {
            db = DBHelper.getInstance(getActivity().getApplicationContext()).getWritableDatabase();
            int year = todolistVo.getYear();
            int month = todolistVo.getMonth();
            int day = todolistVo.getDay();
            String todolist_text = todolistVo.getTodolist_text();
            String groupName = todolistVo.getGroupName();
            int checkcount = todolistVo.getCheckcount();
            int state = todolistVo.getAlarmState();
            db.execSQL("INSERT INTO cleaningTBL (year, month, day, area, task, checkCount, alarmState)" +
                    " VALUES (" + year + ", " + month + ", " + day + ", '" + groupName + "', '" + todolist_text + "', "+ checkcount + ", " + state + ");");
            list.add(new TodolistVo(todolist_text));
            todolistAdapter.notifyDataSetChanged();
            Log.d(TAG, "DB 저장됨");
        }

    //저장된 DB 내용 가져오기 (할일,체크박스 true,false)
        public void selectCleaningArea (String name){
            db = DBHelper.getInstance(getActivity().getApplicationContext()).getWritableDatabase();
            Cursor cursor;
            cursor = db.rawQuery("SELECT task,checkCount FROM cleaningTBL WHERE area="+"'"+name+"';", null);
            list.clear();
            while (cursor.moveToNext()) {

                list.add(new TodolistVo(cursor.getString(0),cursor.getInt(1)));
                Log.d(TAG,"DB에서 select함 내용 : "+ cursor.getString(0)+" / check 유,무 : "+cursor.getInt(1));

            }
            todolistAdapter.notifyDataSetChanged();
            cursor.close();
            Log.d(TAG,"DB에서 select함");
        }

        //레벨 초기값 설정 (초수)
        public void insertLevelInit(){
            db = DBHelper.getInstance(getActivity().getApplicationContext()).getWritableDatabase();
            String level="초수";

            Cursor cursor;
            cursor=db.rawQuery("SELECT * FROM profileTBL",null);
            String nickname=null;
            while (cursor.moveToNext()){

                nickname=cursor.getString(0);
            }
            Log.d(TAG,"닉네임 확인 : "+nickname);


            db.execSQL("UPDATE profileTBL SET Rank='"+level+"' WHERE NickName="+"'"+nickname+"'"+";");

            cursor.close();

            Log.d(TAG, "DB 레벨 업데이트 됨 , 초수");


        }


    //저장된 스코어 가져오기
    public int selectScore(Context context){
        db = DBHelper.getInstance(context.getApplicationContext()).getWritableDatabase();
        Cursor cursor1;
        cursor1=db.rawQuery("SELECT * FROM profileTBL",null);
        String nickname=null;
        while (cursor1.moveToNext()){

            nickname=cursor1.getString(0);
        }
        Log.d(TAG,"닉네임 확인 : "+nickname);

        Cursor cursor;
        cursor = db.rawQuery("SELECT Score FROM profileTBL WHERE NickName="+"'"+nickname+"';", null);
        int score=0;
        while (cursor.moveToNext()){
            score=cursor.getInt(0);

        }
        cursor.close();
        Log.d(TAG,"가져온 score : "+score);
        return score;
    }

    // 알림 on/off
//        public boolean turnAlarm() {
//        db = DBHelper.getInstance(getActivity().getApplicationContext()).getWritableDatabase();
//        Cursor cursor;
//        cursor = db.rawQuery("SELECT task, checkCount FROM notifyTBL WHERE area="+"'"+name+"';", null);
//        list.clear();
//        while (cursor.moveToNext()) {
//
//            list.add(new TodolistVo(cursor.getString(0), cursor.getInt(1)));
//            Log.d(TAG,"DB에서 select함 내용 : "+ cursor.getString(0)+", "+ cursor.getString(1)+", "+cursor.getInt(2));
//
//        }
//
//        todolistAdapter.notifyDataSetChanged();
//        cursor.close();
//        Log.d(TAG,"DB에서 select함");
//        return true;
//    }


    ////////////////////////////////////////알람 세팅 Dialog/////////////////////////////////////////////
    private void alarmSettings(final int position) {

        final View dialogView = View.inflate(getActivity().getApplicationContext(), R.layout.dialog_add_notify, null);
        final android.app.AlertDialog.Builder dlg = new android.app.AlertDialog.Builder(getActivity());
        dlg.setView(dialogView);
        tvTask = dialogView.findViewById(R.id.tvTask);
        timePicker =
                dialogView.findViewById(R.id.timePicker);
        tvDate =
                dialogView.findViewById(R.id.tvDate);
        tvTask.setText(list.get(position).getTodolist_text());
        final ImageView ivCalendar = dialogView.findViewById(R.id.ivCalendar);

        cbMonday = dialogView.findViewById(R.id.cbMonday);
        cbTuesday = dialogView.findViewById(R.id.cbTuesday);
        cbWednesday = dialogView.findViewById(R.id.cbWednesday);
        cbThursday = dialogView.findViewById(R.id.cbThursday);
        cbFriday = dialogView.findViewById(R.id.cbFriday);
        cbSaturday = dialogView.findViewById(R.id.cbSaturday);
        cbSunday = dialogView.findViewById(R.id.cbSunday);
        txtDayCheck = dialogView.findViewById(R.id.txtDayCheck);
        //오늘 날짜 입력부분
        tvDate.setText(String.valueOf(calendar.get(Calendar.YEAR)) + "-" + String.valueOf(calendar.get(Calendar.MONTH) + 1) + "-" + String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        // 날짜선택창 불러오기
        ivCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();

            }
        });

        //요일별 선택 이벤트
//        btnMonday.setOnClickListener(this);
//        btnTuesday.setOnClickListener(this);
//        btnWednesday.setOnClickListener(this);
//        btnThursday.setOnClickListener(this);
//        btnFriday.setOnClickListener(this);
//        btnSaturday.setOnClickListener(this);
//        btnSunday.setOnClickListener(this);

        dlg.setPositiveButton("알림 설정", null);
        dlg.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        toastDisplay("취소되었습니다.");
                    }
                });
        final android.app.AlertDialog alertDialog = dlg.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button button = alertDialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 알람 시간 설정
                        // api 버전별 설정
                        if (Build.VERSION.SDK_INT < 23) {
                            int getHour = timePicker.getCurrentHour();
                            int getMinute = timePicker.getCurrentMinute();
                            calendar.set(Calendar.HOUR_OF_DAY, getHour);
                            calendar.set(Calendar.MINUTE, getMinute);
                            calendar.set(Calendar.SECOND, 0);
                        } else {
                            int getHour = timePicker.getHour();
                            int getMinute = timePicker.getMinute();
                            calendar.set(Calendar.HOUR_OF_DAY, getHour);
                            calendar.set(Calendar.MINUTE, getMinute);
                            calendar.set(Calendar.SECOND, 0);
                        }

                        // 현재보다 이전이면 등록 못하도록
                        if (calendar.before(Calendar.getInstance())) {
                            toastDisplay("알람시간이 현재시간보다 이전일 수 없습니다.");
                            return;
                        }
                        alarmId = selectAlarmId(list.get(position).getTodolist_text());
                        alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
                        //알람 시간 표시
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                        SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm", Locale.getDefault());
                        String date[] = String.valueOf(dbFormat.format(calendar.getTime())).split("-");

                        Intent intent = new Intent(getActivity(), AlarmReceiver.class);
                        intent.putExtra("title", "청소의 달인");
                        intent.putExtra("text", "청소할 시간이군요!");
                        intent.putExtra("id", alarmId);
                        // Receiver 설정
                        PendingIntent pender = PendingIntent.getBroadcast(getActivity(), alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 120*1000,  pender); // 반복알림
                        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pender); // 알람 설정
                        intentArray.add(pender);
                        TodolistVo todolistVo = new TodolistVo (groupText, tvTask.getText().toString(), 1, Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]), Integer.parseInt(date[3]), Integer.parseInt(date[4]), 0);
                        updateAlarm(todolistVo);
                        toastDisplay(String.valueOf(format.format(calendar.getTime())) + " 알림이 설정되었습니다.");
                        alertDialog.dismiss();
                    }
                });
            } // end of onShow
        });
        alertDialog.show();

    } // end of alarmSettings ////////////////////////////////////////////////////////////////////////////////////

    // 알람 등록 모듈
//    private void setAlarm() {
//        // 알람 시간 설정
//        // api 버전별 설정
//        if (Build.VERSION.SDK_INT < 23) {
//            int getHour = timePicker.getCurrentHour();
//            int getMinute = timePicker.getCurrentMinute();
//            calendar.set(Calendar.HOUR_OF_DAY, getHour);
//            calendar.set(Calendar.MINUTE, getMinute);
//            calendar.set(Calendar.SECOND, 0);
//        } else {
//            int getHour = timePicker.getHour();
//            int getMinute = timePicker.getMinute();
//            calendar.set(Calendar.HOUR_OF_DAY, getHour);
//            calendar.set(Calendar.MINUTE, getMinute);
//            calendar.set(Calendar.SECOND, 0);
//        }
//
//        // 현재보다 이전이면 등록 못하도록
//        if (calendar.before(Calendar.getInstance())) {
//            toastDisplay("알람시간이 현재시간보다 이전일 수 없습니다.");
//            return;
//        }
//
//        alarmId = createID();
//        alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
//        //알람 시간 표시
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
//
//        Intent intent = new Intent(getActivity(), AlarmReceiver.class);
//        intent.putExtra("title", "청소의 달인");
//        intent.putExtra("text", "청소할 시간이군요!");
//        intent.putExtra("id", alarmId);
//        pender = PendingIntent.getBroadcast(getActivity(), alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 120*1000,  pender);
//        toastDisplay(String.valueOf(format.format(calendar.getTime())) + " 알림이 설정되었습니다.");
//    }

    // 알람 취소
//    public void cancelAlarm(int alarmId) {
//        Log.d(TAG, "cancelAlarm: " + alarmId);
//        Intent intent = new Intent(context, AlarmReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        alarmManager.cancel(pendingIntent);
//        Log.d(TAG, "cancelAlarm: " + alarmId);
//    }

    //요일 버튼 이벤트
    @Override
    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.btnMonday:
//                if (!btnCheck[0]) {
//                    btnMonday.setBackground(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.btndayofweek_btnon));
//                    if (days != null) {
//                        txtDayCheck.setText("매주 " + days + " " + btnMonday.getText().toString().trim() + "요일에 알람이 울립니다.");
//                    } else {
//                        days = btnMonday.getText().toString().trim();
//                        txtDayCheck.setText("매주 " + days + "요일에 알람이 울립니다.");
//                    }
//                    btnCheck[0] = true;
//                } else {
//                    btnMonday.setBackground(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.btndayofweek_btnoff));
//                    txtDayCheck.setText("매주 " + days.replaceAll("월", "") + "요일에 알람이 울립니다.");
//                    btnCheck[0] = false;
//                }
//                break;
//            case R.id.btnTuesday:
//                if (!btnCheck[1]) {
//                    btnTuesday.setBackground(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.btndayofweek_btnon));
//                    if (days != null) {
//                        txtDayCheck.setText("매주 " + days + " " + btnTuesday.getText().toString().trim() + "요일에 알람이 울립니다.");
//                    } else {
//                        days = btnTuesday.getText().toString().trim();
//                        txtDayCheck.setText("매주 " + days + "요일에 알람이 울립니다.");
//                    }
//                    btnCheck[1] = true;
//                } else {
//                    btnTuesday.setBackground(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.btndayofweek_btnoff));
//                    txtDayCheck.setText("매주 " + days.replaceAll("화", "") + "요일에 알람이 울립니다.");
//                    btnCheck[1] = false;
//                }
//                break;
//            case R.id.btnWednesday:
//                if (!btnCheck[2]) {
//                    btnWednesday.setBackground(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.btndayofweek_btnon));
//                    if (days != null) {
//                        txtDayCheck.setText("매주 " + days + " " + btnWednesday.getText().toString().trim() + "요일에 알람이 울립니다.");
//                    } else {
//                        days = btnWednesday.getText().toString().trim();
//                        txtDayCheck.setText("매주 " + days + "요일에 알람이 울립니다.");
//                    }
//                    btnCheck[2] = true;
//                } else {
//                    btnWednesday.setBackground(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.btndayofweek_btnoff));
//                    txtDayCheck.setText("매주 " + days.replaceAll("수", "") + "요일에 알람이 울립니다.");
//                    btnCheck[2] = false;
//                }
//                break;
//            case R.id.btnThursday:
//                if (!btnCheck[3]) {
//                    btnThursday.setBackground(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.btndayofweek_btnon));
//                    if (days != null) {
//                        txtDayCheck.setText("매주 " + days + " " + btnThursday.getText().toString().trim() + "요일에 알람이 울립니다.");
//                    } else {
//                        days = btnThursday.getText().toString().trim();
//                        txtDayCheck.setText("매주 " + days + "요일에 알람이 울립니다.");
//                    }
//                    btnCheck[3] = true;
//                } else {
//                    btnThursday.setBackground(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.btndayofweek_btnoff));
//                    txtDayCheck.setText("매주 " + days.replaceAll("목", "") + "요일에 알람이 울립니다.");
//                    btnCheck[3] = false;
//                }
//                break;
//            case R.id.btnFriday:
//                if (!btnCheck[4]) {
//                    btnFriday.setBackground(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.btndayofweek_btnon));
//                    if (days != null) {
//                        txtDayCheck.setText("매주 " + days + " " + btnFriday.getText().toString().trim() + "요일에 알람이 울립니다.");
//                    } else {
//                        days = btnFriday.getText().toString().trim();
//                        txtDayCheck.setText("매주 " + days + "요일에 알람이 울립니다.");
//                    }
//                    btnCheck[4] = true;
//                } else {
//                    btnFriday.setBackground(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.btndayofweek_btnoff));
//                    txtDayCheck.setText("매주 " + days.replaceAll("금", "") + "요일에 알람이 울립니다.");
//                    btnCheck[4] = false;
//                }
//                break;
//            case R.id.btnSaturday:
//                if (!btnCheck[5]) {
//                    btnSaturday.setBackground(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.btndayofweek_btnon));
//                    if (days != null) {
//                        txtDayCheck.setText("매주 " + days + " " + btnSaturday.getText().toString().trim() + "요일에 알람이 울립니다.");
//                    } else {
//                        days = btnSaturday.getText().toString().trim();
//                        txtDayCheck.setText("매주 " + days + "요일에 알람이 울립니다.");
//                    }
//                    btnCheck[5] = true;
//                } else {
//                    btnSaturday.setBackground(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.btndayofweek_btnoff));
//                    txtDayCheck.setText("매주 " + days.replaceAll("토", "") + "요일에 알람이 울립니다.");
//                    btnCheck[5] = false;
//                }
//                break;
//            case R.id.btnSunday:
//                if (!btnCheck[6]) {
//                    btnSunday.setBackground(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.btndayofweek_btnon));
//                    if (days != null) {
//                        txtDayCheck.setText("매주 " + days + " " + btnSunday.getText().toString().trim() + "요일에 알람이 울립니다.");
//                    } else {
//                        days = btnSunday.getText().toString().trim();
//                        txtDayCheck.setText("매주 " + days + "요일에 알람이 울립니다.");
//                    }
//                    btnCheck[6] = true;
//                } else {
//                    btnSunday.setBackground(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.btndayofweek_btnoff));
//                    txtDayCheck.setText("매주 " + days.replaceAll("일", "") + "요일에 알람이 울립니다.");
//                    btnCheck[6] = false;
//                }
//                break;
//        }
    }

    private void toastDisplay(String s) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }

    // 날짜 선택 DatePicker Dialog 메소드
    private void showDialog() {
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        DatePickerDialog pickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // 데이터피커에서 선택한 날짜 처리 하는 부분
                tvDate.setText(String.valueOf(format.format(calendar.getTime())));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), (calendar.get(Calendar.DAY_OF_MONTH)));

        pickerDialog.show();
    }

    // 알림 pendingIntent RequestCode 설정
//    public int createID() {
//        Date now = new Date();
//        int id = Integer.parseInt(new SimpleDateFormat("MMddHHmmss", Locale.KOREA).format(now));
//
//        return id;
//    }

    @Override
    public void onAlarmStateChanged(TodolistVo item, int postionInList) {
//        alarmOnOff = item.getState() ? 1 : 0;

    }

    // 알림 설정
    public void updateAlarm(TodolistVo todolistVo) {
        db = DBHelper.getInstance(getActivity().getApplicationContext()).getWritableDatabase();
        int alarmState = todolistVo.getAlarmState();
        int year = todolistVo.getAlarmYear();
        int month = todolistVo.getAlarmMonth();
        int day = todolistVo.getAlarmDay();
        int hour = todolistVo.getAlarmHour();
        int minute = todolistVo.getAlarmMinute();
        int loop = todolistVo.getLoop();
        String area = todolistVo.getGroupName();
        String task = todolistVo.getTodolist_text();
        db.execSQL("UPDATE cleaningTBL SET alarmState = " + alarmState + ", alarmYear = " + year + ", alarmMonth = " + month + ", alarmDay = " + day + ", alarmHour = " + hour + ", AlarmMinute = " + minute + ", loop = " + loop + " WHERE area = '" + area + "' AND task = '" + task + "';");
        Log.d(TAG, "cleaningTBL alarm update");
    }

    // alarmId 불러오기
    public int selectAlarmId(String text) {
        db = DBHelper.getInstance(context).getWritableDatabase();
        Cursor cursor;
        cursor = db.rawQuery("SELECT _ID FROM cleaningTBL WHERE task=" + "'" + text + "' limit 1;", null);
        int alarmId = 0;
        while (cursor.moveToNext()) {
            alarmId = cursor.getInt(0);
            Log.d(TAG, "alarmId select1: " + alarmId);

        }
        Log.d(TAG, "alarmId select2: " + alarmId);
        cursor.close();
        return alarmId;
    }

}
