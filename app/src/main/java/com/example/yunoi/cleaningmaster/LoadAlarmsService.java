package com.example.yunoi.cleaningmaster;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.ArrayList;

public class LoadAlarmsService extends IntentService {

    private static final String TAG = LoadAlarmsService.class.getSimpleName();
    public static final String ACTION_COMPLETE = TAG + ".ACTION_COMPLETE";
    public static final String ALARMS_EXTRA = "alarms_extra";

    @SuppressWarnings("unused")
    public LoadAlarmsService() {
        this(TAG);
    }

    public LoadAlarmsService(String name){
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "onHandleIntent ...");
        final ArrayList<TodolistVo> alarms = DBHelper.getInstance(this).getAlarms();

        final Intent i = new Intent(ACTION_COMPLETE);
        i.putParcelableArrayListExtra(ALARMS_EXTRA, new ArrayList<>(alarms));
        LocalBroadcastManager.getInstance(this).sendBroadcast(i);

    }

    public static void launchLoadAlarmsService(Context context) {
        Log.i(TAG, "launchLoadAlarmsService ...");
        final Intent launchLoadAlarmsServiceIntent = new Intent(context, LoadAlarmsService.class);
        context.startService(launchLoadAlarmsServiceIntent);
    }
}
