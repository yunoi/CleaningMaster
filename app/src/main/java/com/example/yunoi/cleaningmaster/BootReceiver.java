package com.example.yunoi.cleaningmaster;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Re-schedules all stored alarms. This is necessary as {@link AlarmManager} does not persist alarms
 * between reboots.
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
//        if (ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
//            Executors.newSingleThreadExecutor().execute(() -> {
//                final List<NotifyVO> alarms = DBHelper.getInstance(context).getAlarms();
//                setReminderAlarms(context, alarms);
//            });
//        }
    }

}
