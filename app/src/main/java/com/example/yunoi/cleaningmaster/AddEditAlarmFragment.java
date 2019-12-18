//
//package com.example.yunoi.cleaningmaster;
//
//import android.app.DatePickerDialog;
//import android.os.Build;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.CheckBox;
//import android.widget.DatePicker;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.TimePicker;
//import android.widget.Toast;
//
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Locale;
//
//public final class AddEditAlarmFragment extends Fragment implements View.OnClickListener {
//
//    EditText alerEdt;
//    private TimePicker timePicker;
//    private TextView tvDate;
//    private ImageView ivCalendar, ivDown;
//    private Button btnOk, btnCancel, btnSave;
//    private Calendar calendar = Calendar.getInstance(); // 캘린더 인스턴스 생성
//    private LinearLayout notifyLayout; // 알림위해서 켜지는 레이아웃
//    private boolean notifyUpDown = false; // 레이아웃 접기펼치기 플래그
//
//    // 요일 관련 변수
//    private TextView txtDayCheck;
//    private CheckBox cbMon;
//    private CheckBox cbTue;
//    private CheckBox cbWed;
//    private CheckBox cbThu;
//    private CheckBox cbFri;
//    private CheckBox cbSat;
//    private CheckBox cbSun;
//
//    public static Fragment newInstance(TodolistVo alarm) {
//
//        Bundle args = new Bundle();
//        args.putParcelable(AddEditAlarmActivity.ALARM_EXTRA, alarm);
//
//        AddEditAlarmFragment fragment = new AddEditAlarmFragment();
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//
//        final View v = inflater.inflate(R.layout.dialog_add_notify, container, false);
//
//        setHasOptionsMenu(true);
//
//        final TodolistVo alarm = getAlarm();
//
//        alerEdt = v.findViewById(R.id.alert_todolist_alerEdt);
//        timePicker = v.findViewById(R.id.timePicker);
//        tvDate = v.findViewById(R.id.tvDate);
//        ivCalendar = v.findViewById(R.id.ivCalendar);
//        btnOk = v.findViewById(R.id.btnOk);
//        btnCancel = v.findViewById(R.id.btnCancel);
//
//        cbMon = v.findViewById(R.id.cbMonday);
//        cbTue = v.findViewById(R.id.cbTuesday);
//        cbWed = v.findViewById(R.id.cbWednesday);
//        cbThu = v.findViewById(R.id.cbThursday);
//        cbFri = v.findViewById(R.id.cbFriday);
//        cbSat = v.findViewById(R.id.cbSaturday);
//        cbSun = v.findViewById(R.id.cbSunday);
//        txtDayCheck = v.findViewById(R.id.txtDayCheck);
//
//        //오늘 날짜 입력부분
//        tvDate.setText(String.valueOf(calendar.get(Calendar.YEAR)) + "-" + String.valueOf(calendar.get(Calendar.MONTH) + 1) + "-" + String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
//        // 날짜선택창 불러오기
//        ivCalendar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showDialog();
//            }
//        });
//        btnOk.setOnClickListener(this);
//        btnCancel.setOnClickListener(this);
//        ivDown.setOnClickListener(this);
//        btnSave.setOnClickListener(this);
//        return v;
//    }
//
//    private TodolistVo getAlarm() {
//        return getArguments().getParcelable(AddEditAlarmActivity.ALARM_EXTRA);
//    }
//
//    private void setDayCheckboxes(TodolistVo alarm) {
//        cbMon.setChecked(alarm.getDay(TodolistVo.MON));
//        cbTue.setChecked(alarm.getDay(TodolistVo.TUE));
//        cbWed.setChecked(alarm.getDay(TodolistVo.WED));
//        cbThu.setChecked(alarm.getDay(TodolistVo.THU));
//        cbFri.setChecked(alarm.getDay(TodolistVo.FRI));
//        cbSat.setChecked(alarm.getDay(TodolistVo.SAT));
//        cbSun.setChecked(alarm.getDay(TodolistVo.SUN));
//    }
//
//
//    ////////////////////////////////////////알람 세팅 Dialog/////////////////////////////////////////////
//    private void alarmSettings() {
//
//        final TodolistVo alarm = getAlarm();
//
//        setDayCheckboxes(alarm);
//
//        alarm.setLabel(alerEdt.getText().toString());
//        // 알람 시간 설정
//        // api 버전별 설정
//        if (Build.VERSION.SDK_INT < 23) {
//            int getHour = timePicker.getCurrentHour();
//            int getMinute = timePicker.getCurrentMinute();
//            calendar.set(Calendar.HOUR_OF_DAY, getHour);
//            calendar.set(Calendar.MINUTE, getMinute);
//            calendar.set(Calendar.SECOND, 0);
//            alarm.setTime(calendar.getTimeInMillis());
//        } else {
//            int getHour = timePicker.getHour();
//            int getMinute = timePicker.getMinute();
//            calendar.set(Calendar.HOUR_OF_DAY, getHour);
//            calendar.set(Calendar.MINUTE, getMinute);
//            calendar.set(Calendar.SECOND, 0);
//            alarm.setTime(calendar.getTimeInMillis());
//        }
//
//        // 현재보다 이전이면 등록 못하도록
//        if (calendar.before(Calendar.getInstance())) {
//            toastDisplay("알람시간이 현재시간보다 이전일 수 없습니다.");
//            return;
//        }
//        // 요일 설정
//        alarm.setDay(TodolistVo.MON, cbMon.isChecked());
//        alarm.setDay(TodolistVo.TUE, cbTue.isChecked());
//        alarm.setDay(TodolistVo.WED, cbWed.isChecked());
//        alarm.setDay(TodolistVo.THU, cbThu.isChecked());
//        alarm.setDay(TodolistVo.FRI, cbFri.isChecked());
//        alarm.setDay(TodolistVo.SAT, cbSat.isChecked());
//        alarm.setDay(TodolistVo.SUN, cbSun.isChecked());
//
//        final int rowsUpdated = DBHelper.getInstance(getContext()).updateAlarm(alarm);
////        final int messageId = (rowsUpdated == 1) ? R.string.update_complete : R.string.update_failed;
//
//        Toast.makeText(getContext(), "알림이 설정되었습니다.", Toast.LENGTH_SHORT).show();
//
//        AlarmReceiver.setReminderAlarm(getContext(), alarm);
//
//    } // end of alarmSettings ////////////////////////////////////////////////////////////////////////////////////
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.btnOk:
//                alarmSettings();
//                goTodoList();
//                break;
//            case R.id.btnCancel:
//                toastDisplay("취소되었습니다.");
//                goTodoList();
////                delete();
//                break;
//
//        }
//    }
//    public void goTodoList(){
//        Fragment fragment = new TodolistFragment(); //돌아가는 fragment
//        getActivity().getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.coordinatorLayout, fragment)
//                .addToBackStack(null)
//                .commit();
//    }
//
//    private void toastDisplay(String s) {
//        Toast.makeText(getActivity().getApplicationContext(), s, Toast.LENGTH_SHORT).show();
//    }
//
//    // 날짜 선택 DatePicker Dialog 메소드
//    private void showDialog() {
//        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
//        DatePickerDialog pickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
//            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                // 데이터피커에서 선택한 날짜 처리 하는 부분
//                tvDate.setText(String.valueOf(format.format(calendar.getTime())));
//            }
//        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), (calendar.get(Calendar.DAY_OF_MONTH)));
//
//        pickerDialog.show();
//    }
//
//    private void delete() {
//        final TodolistVo alarm = getAlarm();
//
//        //Cancel any pending notifications for this alarm
//        AlarmReceiver.cancelReminderAlarm(getContext(), alarm);
//
//        final int rowsDeleted = DBHelper.getInstance(getContext()).deleteAlarm(alarm);
//        getActivity().finish();
//    }
//
//    private TodolistVo getAlarm() {
//        final long id = DBHelper.getInstance(getActivity()).addAlarm();
//        LoadAlarmsService.launchLoadAlarmsService(getActivity());
//        return new TodolistVo(id);
//    }
//}
//
