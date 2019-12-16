//package com.example.yunoi.cleaningmaster;
//
//import android.annotation.SuppressLint;
//import android.app.Notification;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.Service;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Build;
//import android.os.IBinder;
//import android.support.annotation.RequiresApi;
//import android.support.v4.app.NotificationCompat;
//import android.util.Log;

//public class AlarmService extends Service {
//
//    private static final String TAG = "AlarmService";
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//    @SuppressLint("InvalidWakeLockTag")
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//            String channelId = createNotificationChannel();
//
//            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId);
//            Notification notification = builder.setOngoing(true)
//                    .setSmallIcon(R.mipmap.ic_launcher)
//                    //.setCategory(Notification.CATEGORY_SERVICE)
//                    .build();
//            startForeground(startId, notification);
//        }
//
//        Log.d(TAG, "Alarm");
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//            stopForeground(true);
//        }
//
//        stopSelf();
//
//        return START_NOT_STICKY;
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    private String createNotificationChannel() {
//        String channelId = "Alarm";
//        String channelName = getString(R.string.app_name);
//        NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
//        //channel.setDescription(channelName);
//        channel.setSound(null, null);
//        channel.enableVibration(true);
//        channel.setVibrationPattern(new long[]{500,500});
//        channel.setShowBadge(true);
//        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
//        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        manager.createNotificationChannel(channel);
//
//
//        return channelId;
//    }
//}
