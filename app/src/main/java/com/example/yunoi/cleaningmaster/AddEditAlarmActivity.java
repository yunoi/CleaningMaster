package com.example.yunoi.cleaningmaster;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AddEditAlarmActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String ALARM_EXTRA = "alarm_extra";
    public static final String MODE_EXTRA = "mode_extra";
    public static final String TAG = "AddEditAlarmActivity";


    @Retention(RetentionPolicy.SOURCE)
    ////// ADD_ALARM : 쓰이지 않음!!!
    @IntDef({EDIT_ALARM, ADD_ALARM, UNKNOWN})
    @interface Mode {
    }

    public static final int EDIT_ALARM = 1;
    public static final int ADD_ALARM = 2;
    public static final int UNKNOWN = 0;

    EditText alerEdt;
    private TimePicker timePicker;
    private TextView tvTask;
    private Button btnOk, btnCancel;
    private Calendar calendar = Calendar.getInstance(); // 캘린더 인스턴스 생성

    // 요일 관련 변수
    private CheckBox cbMon;
    private CheckBox cbTue;
    private CheckBox cbWed;
    private CheckBox cbThu;
    private CheckBox cbFri;
    private CheckBox cbSat;
    private CheckBox cbSun;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_notify);
        Log.i(getClass().getSimpleName(), "onCreate ...");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getToolbarTitle());

        final AlarmVO alarm = getAlarm();
        Log.d(TAG, "onCreate. getAlarm() : "+alarm.toString());

        alerEdt = findViewById(R.id.alert_todolist_alerEdt);
        timePicker = findViewById(R.id.timePicker);
        tvTask = findViewById(R.id.tvTask);
        btnOk = findViewById(R.id.btnOk);
        btnCancel = findViewById(R.id.btnCancel);

        cbMon = findViewById(R.id.cbMonday);
        cbTue = findViewById(R.id.cbTuesday);
        cbWed = findViewById(R.id.cbWednesday);
        cbThu = findViewById(R.id.cbThursday);
        cbFri = findViewById(R.id.cbFriday);
        cbSat = findViewById(R.id.cbSaturday);
        cbSun = findViewById(R.id.cbSunday);

        tvTask.setText(getAlarm().getLabel());

        setDayCheckboxes(alarm);

        btnOk.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }


    private @Mode
    int getMode() {
        final @Mode int mode = getIntent().getIntExtra(MODE_EXTRA, UNKNOWN);
        return mode;
    }

    private String getToolbarTitle() {
        String titleResId;
        switch (getMode()) {
            case EDIT_ALARM:
                titleResId = "알림 설정";
                break;
            case ADD_ALARM:
                titleResId = "알림 설정";
            break;
            case UNKNOWN:
            default:
                throw new IllegalStateException("Mode supplied as intent extra for " +
                        AddEditAlarmActivity.class.getSimpleName() + " must match value in " +
                        Mode.class.getSimpleName());
        }
        return titleResId;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public static Intent buildAddEditAlarmActivityIntent(Context context, @Mode int mode) {
        final Intent i = new Intent(context, AddEditAlarmActivity.class);
        i.putExtra(MODE_EXTRA, mode);
        return i;
    }

    private void setDayCheckboxes(AlarmVO alarm) {
        cbMon.setChecked(alarm.getDay(AlarmVO.MON));
        cbTue.setChecked(alarm.getDay(AlarmVO.TUE));
        cbWed.setChecked(alarm.getDay(AlarmVO.WED));
        cbThu.setChecked(alarm.getDay(AlarmVO.THU));
        cbFri.setChecked(alarm.getDay(AlarmVO.FRI));
        cbSat.setChecked(alarm.getDay(AlarmVO.SAT));
        cbSun.setChecked(alarm.getDay(AlarmVO.SUN));
    }


    ////////////////////////////////////////알람 세팅 Dialog/////////////////////////////////////////////
    private void alarmSettings() {

        final AlarmVO alarm = getAlarm();
        Log.d(TAG, "alarmSettings. getAlarm() : "+alarm.toString());

        // 알람 시간 설정
        // api 버전별 설정
        if (Build.VERSION.SDK_INT < 23) {
            int getHour = timePicker.getCurrentHour();
            int getMinute = timePicker.getCurrentMinute();
            calendar.set(Calendar.HOUR_OF_DAY, getHour);
            calendar.set(Calendar.MINUTE, getMinute);
            calendar.set(Calendar.SECOND, 0);
            alarm.setTime(calendar.getTimeInMillis());
        } else {
            int getHour = timePicker.getHour();
            int getMinute = timePicker.getMinute();
            calendar.set(Calendar.HOUR_OF_DAY, getHour);
            calendar.set(Calendar.MINUTE, getMinute);
            calendar.set(Calendar.SECOND, 0);
            alarm.setTime(calendar.getTimeInMillis());
        }


        Log.d(TAG, "alarmSettings. time: "+ alarm.getTime());

        alarm.setLabel(tvTask.getText().toString());

        Log.d(TAG, "alarmSettings. task: "+ alarm.getLabel());

        // 요일 설정
        alarm.setDay(AlarmVO.MON, cbMon.isChecked());
        alarm.setDay(AlarmVO.TUE, cbTue.isChecked());
        alarm.setDay(AlarmVO.WED, cbWed.isChecked());
        alarm.setDay(AlarmVO.THU, cbThu.isChecked());
        alarm.setDay(AlarmVO.FRI, cbFri.isChecked());
        alarm.setDay(AlarmVO.SAT, cbSat.isChecked());
        alarm.setDay(AlarmVO.SUN, cbSun.isChecked());

        if(!(cbMon.isChecked() || cbTue.isChecked() || cbWed.isChecked() || cbThu.isChecked() || cbFri.isChecked() || cbSat.isChecked() || cbSun.isChecked())) {
            alarm.setIsEnabled(false);
        } else {
            alarm.setIsEnabled(true);
        }

        final int rowsUpdated = DBHelper.getInstance(this).updateAlarm(alarm);
        final String messageId = (rowsUpdated == 1) ? "알림이 설정되었습니다." : "알람설정을 실패하였습니다.";
        Log.d(TAG, "alarmSettings. updateAlarm : "+messageId);
        Toast.makeText(this, messageId, Toast.LENGTH_SHORT).show();

        AlarmReceiver.setReminderAlarm(this, alarm);


    } // end of alarmSettings ////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnOk:
                alarmSettings();
                finish();

                break;
            case R.id.btnCancel:
                toastDisplay("취소되었습니다.");
                finish();
                break;

        }
    }


    private void toastDisplay(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }

private AlarmVO getAlarm() {
    switch (getMode()) {
        case EDIT_ALARM:
            return getIntent().getParcelableExtra(ALARM_EXTRA);
        case ADD_ALARM:
            final long id = DBHelper.getInstance(this).addAlarm();
            LoadAlarmsService.launchLoadAlarmsService(this);
            return new AlarmVO(id);
        case UNKNOWN:
        default:
            throw new IllegalStateException("Mode supplied as intent extra for " +
                    AddEditAlarmActivity.class.getSimpleName() + " must match value in " +
                    Mode.class.getSimpleName());
    }
}

    // 청소구역 한번에 지울때만
    public void delete() {
        final AlarmVO alarm = getIntent().getParcelableExtra("delete_alarm");
        Log.d(TAG, "intent.deleted_alarm : " + alarm.toString());
        //Cancel any pending notifications for this alarm
        AlarmReceiver.cancelReminderAlarm(this, alarm);

        final int rowsDeleted = DBHelper.getInstance(this).deleteAlarm(alarm);
        Log.d(TAG, "deleted row: " + rowsDeleted);
    }
}