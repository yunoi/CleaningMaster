package com.example.yunoi.cleaningmaster;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import static android.app.Notification.VISIBILITY_PUBLIC;

public class AlarmReceiver extends BroadcastReceiver {

    private static final String TAG = "AlarmReceiver";
    private int alarmId = 0;

//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("InvalidWakeLockTag")
    @Override
    public void onReceive(Context context, Intent intent) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String title = intent.getStringExtra("title");
            String text = intent.getStringExtra("text");
            alarmId = intent.getIntExtra("id", 0);
            Intent rIntent = new Intent(context, AlarmService.class);
            PendingIntent pend = PendingIntent.getActivity(context, alarmId, new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
            context.startForegroundService(rIntent);
            //NotificationManager 안드로이드 상태바에 메세지를 던지기위한 서비스 불러오고
            NotificationManager notificationmanager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
            Notification.Builder builder = new Notification.Builder(context);
            builder.setSmallIcon(R.drawable.ic_launcher_background)
                    .setWhen(System.currentTimeMillis())
                    .setContentTitle(title)
                    .setContentText(text)
                    .setChannelId("Alarm")
                    .setCategory(NotificationCompat.CATEGORY_REMINDER)
                    .setFullScreenIntent(pend, true)
                    .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                    .setContentIntent(pend)
                    .setVisibility(VISIBILITY_PUBLIC)
                    .setAutoCancel(true);
            WakeLocker.acquire(context);
            notificationmanager.notify(alarmId, builder.build());

        } else {
            String title = intent.getStringExtra("title");
            String text = intent.getStringExtra("text");
            alarmId = intent.getIntExtra("id", 0);
            Intent rIntent = new Intent(context, AlarmService.class);
            PendingIntent pend = PendingIntent.getActivity(context, alarmId, new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
            context.startService(rIntent);
            NotificationManager mNotificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.ic_launcher_background)
                            .setContentTitle(title)
                            .setContentText(text)
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setCategory(NotificationCompat.CATEGORY_REMINDER)
                            .setFullScreenIntent(pend, true)
                            .setContentIntent(pend)
                            .setAutoCancel(true)
                            .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                            .setVisibility(VISIBILITY_PUBLIC);

            WakeLocker.acquire(context);
            mNotificationManager.notify(alarmId, mBuilder.build());

        }

        WakeLocker.release();
    }
    // 알림 pendingIntent RequestCode 설정
//    public int createID(){
//        Date now = new Date();
//        int id = Integer.parseInt(new SimpleDateFormat("ddHHmmss", Locale.KOREA).format(now));
//
//        return id;
//    }
}
