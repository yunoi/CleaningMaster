package com.example.yunoi.cleaningmaster;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.PowerManager;

public class WakeLocker{
    private static PowerManager.WakeLock wakeLock;
    private static final String TAG = "WakeLocker";

    @SuppressLint("InvalidWakeLockTag")
    public static void acquire(Context context){
        if(wakeLock != null){
            wakeLock.release();
        }
        PowerManager powerManager = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK|
                PowerManager.ACQUIRE_CAUSES_WAKEUP|
                PowerManager.ON_AFTER_RELEASE, TAG);

        wakeLock.acquire(); // 휴대폰이 대기상태로 가지 않도록 유지
    }
    public static void release(){
        if(wakeLock != null){
            wakeLock.release();
            wakeLock = null;
        }
    }
}
