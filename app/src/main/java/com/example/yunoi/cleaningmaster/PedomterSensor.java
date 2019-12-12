package com.example.yunoi.cleaningmaster;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

public class PedomterSensor extends Service implements SensorEventListener{

    final static String MY_ACTION="com.example.yunoi.cleaningmaster.PedomterSensor.MY_ACTION";
    static String reading; // 걸음값
    static String readkcal; // 칼로리값
    static float KCAL= 0.03f; // 한걸음당 칼로리
    static Sensor sensor ;
    static SensorManager sensorManager;
    private List<Sensor> sensorList;
    private float startStepCount=0.0f;
    private float stepCount=0.0f;
    private boolean flag=false;
    int i=0;
    static final String LOG_TAG="Sensor";
    Intent intent = new Intent("com.example.yunoi.cleaningmaster.PedomterSensor.MY_ACTION");
    AlarmManager alarmManager;


    // 내부클래스 생성
    public class Detect extends AppCompatActivity {

        // 앱을 재개 시키는 함수
        @Override
        protected void onResume() {
            super.onResume();
            sensorManager.registerListener((SensorEventListener) this,sensor,sensorManager.SENSOR_DELAY_NORMAL);
        }
        // 앱을 일시정지
        @Override
        protected void onPause() {
            super.onPause();
        }


    }
    //============================================================
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG,"onStartCommand()");
        // 센서값을 가져오기
        sensorManager=(SensorManager)getSystemService(SENSOR_SERVICE);
        // 센서의 값을 담는 배열을 만든다
        sensorList=sensorManager.getSensorList(Sensor.TYPE_STEP_COUNTER);
        // 센서의 값을 List에 넣는다 ( for-each활용)
        for(Sensor sensor : sensorList){
            // 센서 매니저를 등록시키고 현재 센서의 매니저 객체가 SENSOR_NORMAL 만큼 값을 읽는다
            sensorManager.registerListener(this,sensor,sensorManager.SENSOR_DELAY_NORMAL);
        }
        flag=true;

    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        PedomterNotiControl control = new PedomterNotiControl(getApplicationContext());
        startForeground(1,control.getNoti());
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG,"onDestroy()");
        sensorManager.unregisterListener(this);
        flag=false;
        startStepCount=0.0f;
        stepCount=0.0f;
        KCAL=0.03f;
        Log.d(LOG_TAG,"onDestroy()");

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if(flag==true){
            if(event.sensor.getType() == Sensor.TYPE_STEP_COUNTER){
                startStepCount=event.values[0];


                flag=false;
            }
        }
        if(event.sensor.getType() == Sensor.TYPE_STEP_COUNTER){
            stepCount=event.values[0];
            Log.d("SensorService","걸음수:"+event.values[0]);
            // 쉐어더 프리프런스.
            Log.d(LOG_TAG,"onSensorChanged");
            // 문자열을 합쳐준다
            StringBuilder builder = new StringBuilder();
            for(int i =0 ; i < event.values.length ; i ++){
                builder.append("걸");
                builder.append("음");
                builder.append("수");
                builder.append(event.values[i]);
                builder.append("\n");
                builder.append(i);
            }


//            String num = String.format("%.2f",KCAL);
            reading=builder.toString();
            reading= String.valueOf(stepCount-startStepCount);
            KCAL=(float)(Math.round(KCAL*100)/100.0);
            readkcal=builder.toString();
            readkcal=String.valueOf( (stepCount-startStepCount)*KCAL);




            //인탠트 값을 보낸다
            intent.putExtra("ksm",reading);
            intent.putExtra("ksm2",readkcal);
            sendBroadcast(intent);



        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void registerRestartAlarm(boolean isOn){
        Intent intent0 = new Intent(PedomterSensor.this,PedomterFrgment.MyReceiver.class);
        intent0.setAction(PedomterFrgment.MyReceiver.ACTION_RESTART_SERVICE);
        PendingIntent sender = PendingIntent.getBroadcast(getApplicationContext(),0,intent0,0);
        AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
        if(isOn){
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,SystemClock.elapsedRealtime()
                    +1000,10000,sender);
        }else{
            alarmManager.cancel(sender);
        }
    }
    public void toastDisplay(String s) {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
        if(sensorList == null){
            toastDisplay("센서 null");
        }else{
            toastDisplay("센서 Open");
        }
    }
}
