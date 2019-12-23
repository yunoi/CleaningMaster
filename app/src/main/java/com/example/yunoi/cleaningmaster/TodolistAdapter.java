package com.example.yunoi.cleaningmaster;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;

import java.util.ArrayList;
import lib.mozidev.me.extextview.ExTextView;
public class TodolistAdapter extends RecyclerView.Adapter<TodolistAdapter.CustomViewHolder> {

    private Context context;
    private int layout;
    private ArrayList<AlarmVO> alarmList = new ArrayList<>();
    private SQLiteDatabase db;
    private alarmClickListener listener = null;
    private String[] mDays;
    private static final String TAG = "Adapter";
    private int selectedColor;


    //생성자
    public TodolistAdapter() {
    }

    public TodolistAdapter(Context context, int layout, ArrayList<AlarmVO> alarmList) {
        this.context = context;
        this.layout = layout;
        this.alarmList = alarmList;
    }

    // 클릭 리스너 인터페이스
    public interface alarmClickListener {
        void onAlarmClick(View v, int position);
    }

    public void setAlarmClickListener(alarmClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public TodolistAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(layout, viewGroup, false);
        CustomViewHolder customViewHolder = new CustomViewHolder(view);

        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final TodolistAdapter.CustomViewHolder customViewHolder, final int position) {
        final Context context = customViewHolder.itemView.getContext();



        if (mDays == null) {
            mDays = context.getResources().getStringArray(R.array.days_abbreviated);
        }


        final AlarmVO alarm = alarmList.get(position);
        customViewHolder.todolist_text.setText(alarm.getLabel());
        customViewHolder.todolist_alramClocktxt.setText(AlarmUtils.getReadableTime(alarm.getTime()) + " " + AlarmUtils.getAmPm(alarm.getTime()));
        customViewHolder.todolist_alramReaptTxt.setText(buildSelectedDays(alarm));
        customViewHolder.todolist_alram.setBackgroundResource(imageChange(alarm));
        customViewHolder.todolist_text.setTag(customViewHolder);
        customViewHolder.todolist_alramClocktxt.setTag(customViewHolder);
        customViewHolder.todolist_alramReaptTxt.setTag(customViewHolder);
        Log.d(TAG, "onBindViewHolder. alarm : "+ alarm.toString());

        //취소 버튼 액션
        customViewHolder.swipe_sample1.setShowMode(SwipeLayout.ShowMode.LayDown);
        customViewHolder.swipe_sample1.findViewById(R.id.delete_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                //취소 알런트
                final View alertDialogView = View.inflate(v.getContext(), R.layout.dialog_delete_layout, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext(), R.style.MyDialogTheme);
                builder.setView(alertDialogView);
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String text = alarmList.get(position).getLabel();

                        final AlarmVO alarm = alarmList.get(position);

                        Log.d(TAG, "for alarm delete position : "+position);
                        Log.d(TAG, "for alarm delete : "+alarmList.get(position).toString());
                        // 알림삭제 (Cancel any pending notifications for this alarm)
                        AlarmReceiver.cancelReminderAlarm(context, alarm);
                        alarmList.remove(position);
                        notifyDataSetChanged();
                        deleteCleningArea(text, v.getContext()); //DB 삭제부분
                        final int rowsDeleted = DBHelper.getInstance(context).deleteAlarm(alarm);  // 알람 DB삭제

                        Snackbar snackbar = Snackbar.make(customViewHolder.todo_linearLayout, "삭제되었습니다!", Snackbar.LENGTH_SHORT);
                        snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                        View snackbarView = snackbar.getView();
                        TextView tv = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                        tv.setTextColor(Color.WHITE);

                        snackbarView.setBackgroundColor(Color.parseColor("#024873"));
                        snackbar.show();

                        LoadAlarmsService.launchLoadAlarmsService(context);

                    }

                });
                builder.setNegativeButton("취소", null);
                builder.show();
            }
        });

        // 알람 설정 버튼 액션
        customViewHolder.todolist_alram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onAlarmClick(v, position);

                }
            }
        });

        customViewHolder.todolist_alram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Context c = view.getContext();
                final Intent launchEditAlarmIntent =
                        AddEditAlarmActivity.buildAddEditAlarmActivityIntent(
                                c, AddEditAlarmActivity.EDIT_ALARM
                        );
                launchEditAlarmIntent.putExtra(AddEditAlarmActivity.ALARM_EXTRA, alarm);
                c.startActivity(launchEditAlarmIntent);
            }
        });
    }   // end of onBindViewHolder

    @Override
    public int getItemCount() {
        return alarmList != null ? alarmList.size() : 0;
    }

    private Spannable buildSelectedDays(AlarmVO alarm) {
        selectedColor = ContextCompat.getColor(context, R.color.colorPrimaryDark);
        int primaryColor = ContextCompat.getColor(context, R.color.colorAccent);
        final int numDays = 7;
        final SparseBooleanArray days = alarm.getDays();

        final SpannableStringBuilder builder = new SpannableStringBuilder();
        ForegroundColorSpan span;

        int startIndex, endIndex;
        for (int i = 0; i < numDays; i++) {

            startIndex = builder.length();

            final String dayText = mDays[i];
            builder.append(dayText);
            builder.append(" ");

            endIndex = startIndex + dayText.length();

            final boolean isSelected = days.valueAt(i);
            if (isSelected) {
                span = new ForegroundColorSpan(primaryColor);
                builder.setSpan(span, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        return builder;

    }

    public int imageChange(AlarmVO alarm){
        if(alarm.isEnabled()) {
            Log.d(TAG, "imageChange value == null? : "+alarm.toString());
            return R.drawable.alarmrepeat;
        } else {
            return R.drawable.alram;
        }
    }

    public void setAlarms(ArrayList<AlarmVO> alarms) {
        Log.d(TAG, "setAlarms");
//        DBHelper.getInstance(context).areaSort();
        alarmList = alarms;
        notifyDataSetChanged();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        private ImageButton todolist_alram;
        private ExTextView todolist_text;
        private SwipeLayout swipe_sample1;
        private LinearLayout todo_linearLayout;
        private TextView todolist_alramReaptTxt, todolist_alramClocktxt;


        public CustomViewHolder(@NonNull final View itemView) {
            super(itemView);
            todolist_alram = itemView.findViewById(R.id.todolist_alram);
            todolist_text = itemView.findViewById(R.id.todolist_text);
            swipe_sample1 = itemView.findViewById(R.id.swipe_sample1);
            todo_linearLayout = itemView.findViewById(R.id.todo_linearLayout);
            todolist_alramReaptTxt = itemView.findViewById(R.id.todolist_alramReaptTxt);
            todolist_alramClocktxt = itemView.findViewById(R.id.todolist_alramClocktxt);

            todolist_alram.setBackgroundResource(R.drawable.alram);
        }   // end of constructor

    } // end of customViewHolder class


    // 청소리스트 DB 삭제 부분
    public void deleteCleningArea(String text, Context context) {
        db = DBHelper.getInstance(context).getWritableDatabase();
        db.execSQL("DELETE FROM cleaningTBL WHERE task='" + text + "';");
        Log.d(TAG, "DB 삭제됨");

    }

}
