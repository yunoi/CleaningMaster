package com.example.yunoi.cleaningmaster;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import java.util.Date;
import java.util.Locale;

import static android.content.Context.ALARM_SERVICE;

public class TodolistFragment extends Fragment {

    View view;
    private SQLiteDatabase db;

    private ArrayList<TodolistVo> list = new ArrayList<TodolistVo>();
    private TodolistAdapter todolistAdapter;
    private LinearLayoutManager linearLayoutManager;

    private String groupText; //구역이름
    public static int taskcount = 0; //그 구역의 총 리스트 사이즈

    // 알림 관련 변수
    private Calendar calendar = Calendar.getInstance(); // 캘린더 인스턴스 생성
    private AlarmManager alarmManager;
    private TimePicker timePicker;
    private PendingIntent pender;
    private TextView tvDate;
    private int alarmId = 0;

    // 요일 관련 변수
    private boolean btnCheck = false;
    private TextView txtDayCheck;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.todolist_fragment, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.todo_listView);

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

        //mainFragment 에서 번들값 받아옴.

        if (getArguments() != null) {
            String groupText = getArguments().getString("groupText");
            actionbar_todoText.setText(groupText);
        }

        //리싸이클러뷰 설정
        linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        todolistAdapter = new TodolistAdapter(R.layout.todo_list_holder_layout, list);
        recyclerView.setAdapter(todolistAdapter);

        //list 추가 버튼 -> 알런트 창 -> 확인 -> list 추가

        actionbar_todoBtnAddlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final View alertDialogView = View.inflate(v.getContext(), R.layout.dialog_add_todolist, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext(), R.style.MyDialogTheme);
                builder.setView(alertDialogView);
                builder.setPositiveButton("저장", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        taskcount++;
                        EditText alerEdt = alertDialogView.findViewById(R.id.alert_todolist_alerEdt);
                        list.add(new TodolistVo(groupText, alerEdt.getText().toString()));
                        todolistAdapter.notifyDataSetChanged();

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
                alarmSettings();
            }

            @Override
            public void onSwitchClick(View v, int position) {

            }
        });

        return view;
    }
////////////////////////////////////////알람 세팅 Dialog/////////////////////////////////////////////
    private void alarmSettings() {

        final View dialogView = View.inflate(getActivity().getApplicationContext(), R.layout.dialog_add_notify, null);
        final android.app.AlertDialog.Builder dlg = new android.app.AlertDialog.Builder(getActivity());
        dlg.setView(dialogView);
        timePicker =
                dialogView.findViewById(R.id.timePicker);
        tvDate =
                dialogView.findViewById(R.id.tvDate);
        final ImageView ivCalendar = dialogView.findViewById(R.id.ivCalendar);
        final Button btnMonday = dialogView.findViewById(R.id.btnMonday);
        final Button btnTuesday = dialogView.findViewById(R.id.btnTuesday);
        final Button btnWednesday = dialogView.findViewById(R.id.btnWednesday);
        final Button btnThursday = dialogView.findViewById(R.id.btnThursday);
        final Button btnFriday = dialogView.findViewById(R.id.btnFriday);
        final Button btnSaturday = dialogView.findViewById(R.id.btnSaturday);
        final Button btnSunday = dialogView.findViewById(R.id.btnSunday);
        txtDayCheck = dialogView.findViewById(R.id.txtDayCheck);

        // 날짜선택창 불러오기
        ivCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();

            }
        });

        //요일별 선택 이벤트
        btnMonday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnBackGroundChange(btnMonday);
            }
        });
        btnTuesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnBackGroundChange(btnTuesday);

            }
        });
        btnWednesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnBackGroundChange(btnWednesday);

            }
        });
        btnThursday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnBackGroundChange(btnThursday);

            }
        });
        btnFriday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnBackGroundChange(btnFriday);

            }
        });
        btnSaturday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnBackGroundChange(btnSaturday);

            }
        });
        btnSunday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnBackGroundChange(btnSunday);

            }
        });
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
                        alarmId = createID();
                        alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
                        //알람 시간 표시
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

                        Intent intent = new Intent(getActivity(), AlarmReceiver.class);
                        intent.putExtra("title", "청소의 달인");
                        intent.putExtra("text", "청소할 시간이군요!");
                        intent.putExtra("id", alarmId);
                        pender = PendingIntent.getBroadcast(getActivity(), alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pender);
                        toastDisplay(String.valueOf(format.format(calendar.getTime())) + " 알림이 설정되었습니다.");
                        alertDialog.dismiss();
                    }
                });
            } // end of onShow
        });
        alertDialog.show();

    } // end of alarmSettings ////////////////////////////////////////////////////////////////////////////////////

    //요일 버튼 이벤트
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void btnBackGroundChange(Button btn) {
        if (!btnCheck) {
            btn.setBackground(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.btndayofweek_btnon));
            txtDayCheck.setText("매주 " + btn.getText().toString().trim() + "요일에 알람이 울립니다.");
            btnCheck = true;
        } else {
            btn.setBackground(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.btndayofweek_btnoff));
            txtDayCheck.setText("매일 알림");
            btnCheck = false;
        }
    }

    private void toastDisplay(String s) {
        Toast.makeText(getActivity().getApplicationContext(), s, Toast.LENGTH_SHORT).show();

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
    public int createID(){
        Date now = new Date();
        int id = Integer.parseInt(new SimpleDateFormat("ddHHmmss", Locale.KOREA).format(now));

        return id;
    }
    // 알람 등록
    private void setAlarm() {
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

        alarmId = createID();
        alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
        //알람 시간 표시
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        Intent intent = new Intent(getActivity(), AlarmReceiver.class);
        intent.putExtra("title", "청소의 달인");
        intent.putExtra("text", "청소할 시간이군요!");
        intent.putExtra("id", alarmId);
        pender = PendingIntent.getBroadcast(getActivity(), alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pender);

        toastDisplay(String.valueOf(format.format(calendar.getTime())) + " 알림이 설정되었습니다.");
    }

    // 알람 취소
    private void cancelAlarm() {
        if (pender != null) {
            alarmManager.cancel(pender);
        }
        toastDisplay("알람이 취소되었습니다.");
    }
}
