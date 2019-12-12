package com.example.yunoi.cleaningmaster;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

public class PedomterNotiControl  {

    private Context context;
    private RemoteViews remoteViews;
    private NotificationCompat.Builder newBuilder;

    public PedomterNotiControl(Context parent) {
        this.context = parent;
        newBuilder = new NotificationCompat.Builder(context).setContentTitle("만보기")
                .setSmallIcon(R.mipmap.run)
                .setPriority(Notification.PRIORITY_MIN)
                .setOngoing(true);
        remoteViews = new RemoteViews(parent.getPackageName(), R.layout.pednoticontro_xml);
        setListener(remoteViews);
        newBuilder.setContent(remoteViews);
    }

    public Notification getNoti(){
        return newBuilder.build();
    }

    private void setListener(RemoteViews remoteViews) {
        Intent intent = new Intent(context,PedomterNotiControlView.class);
        intent.putExtra("Foo","bar");
        PendingIntent button = PendingIntent.getActivity(context,0,intent,0);
        remoteViews.setOnClickPendingIntent(R.id.btn,button);
    }

}


