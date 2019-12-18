package com.example.yunoi.cleaningmaster;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.dinuscxj.progressbar.CircleProgressBar;
import com.facebook.stetho.Stetho;

import java.util.ArrayList;
import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;
import static com.example.yunoi.cleaningmaster.PedomterSensor.MY_ACTION;
import static com.example.yunoi.cleaningmaster.PedomterSensor.reading;
import static com.example.yunoi.cleaningmaster.PedomterSensor.readkcal;

public class PedomterFrgment extends Fragment implements View.OnClickListener {
    private View view;
    static final String LOG_TAG="PedomterFrgment";
    // 프래그먼트
    MyReceiver myReceiver = null;
    private PedomterFrgment pedomterFrgment;
    private CircleProgressBar day_Graph=null;
    private CircleProgressBar day_Kcal=null;
    private CircleProgressBar day_Min=null;
    private Context mContext;
    private Activity activity;
    private Thread progressThread;
    static PedomterSensor sensorService;
    static Chronometer chronometer;
    private TextView textStep , textKcal , textMinute;
    ImageButton ivBtnBar , ivbtnTwo;
    Intent intent;
    final static int[] timeProgress = {0};
    ArrayList<String> list = new ArrayList<>();
    ArrayList<String> listData = new ArrayList<>();
    ArrayList<PedColumnVO> list2 = new ArrayList<>();
    DBHelper dbHelper;
    SQLiteDatabase db;
    Calendar calendar;
    String ksm;
    String ksm2;
    boolean check=false;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.pedmain0207_xml,container,false);
        customActionBar(inflater);
        textStep=view.findViewById(R.id.textStep);
        textKcal=view.findViewById(R.id.textKcal);
        textMinute=view.findViewById(R.id.textMinute);
        day_Graph=view.findViewById(R.id.day_Graph);
        day_Kcal=view.findViewById(R.id.day_Kcal);
        day_Min=view.findViewById(R.id.day_Min);
        ivBtnBar=view.findViewById(R.id.ivBtnBar);
        ivbtnTwo=view.findViewById(R.id.ivbtnTwo);
        chronometer=view.findViewById(R.id.chronometer);
        // 저장된 값을 불러오기 위한 같은 네임파일을 찾는다
        SharedPreferences sharedPreferences =activity.getSharedPreferences("dSave",MODE_PRIVATE);
        //text라는 key에 저장된 값이 있는지 확인 , 아무값 도 없으면 ""를 반환
        String text=sharedPreferences.getString("text","");
        day_Graph.setMax(1000);
        day_Kcal.setMax(1000);
        textStep.setText(text);
        ivBtnBar.setOnClickListener(this);
        ivbtnTwo.setOnClickListener(this);
        calendar=Calendar.getInstance();
     //   CheckInsertData();
     //   Stetho.initializeWithDefaults(this);
        return view;
    }

    public boolean CheckInsertData() {
//        int cDay = calendar.get(Calendar.DAY_OF_MONTH);
//        int bDay = cDay - 7;
        int aYear = calendar.get(Calendar.YEAR);
        int bMonth=calendar.get(Calendar.MONTH+1);
        int cDay=calendar.get(Calendar.DAY_OF_MONTH);
        db = DBHelper.getInstance(getActivity().getApplicationContext()).getWritableDatabase();
        ArrayList<Integer> checkCalendar = new ArrayList<>();
        Cursor curCalendar = db.rawQuery("SELECT year , month , day FROM PedTBL WHERE year= "+ aYear +" AND  month= "+bMonth+" AND day = " +cDay+";", null);
        while (curCalendar.moveToNext()){
            curCalendar.getString(0);
            curCalendar.getString(1);
            curCalendar.getString(2);

//           checkCalendar.add(Integer.parseInt(curCalendar.getString(0)));
//           checkCalendar.add(Integer.parseInt(curCalendar.getString(1)));
//           checkCalendar.add(Integer.parseInt(curCalendar.getString(2)));
        }
        if(checkCalendar.size() > 0){
            check=false; // 데이터 x
        }else{
            check=true; // 데이터 ㅇ
        }
        return check;



    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
        if(context instanceof Activity){
            activity=(Activity)context;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(LOG_TAG,"onResume/registering receiver");
        myReceiver= new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(MY_ACTION);
        activity.registerReceiver(myReceiver,filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(LOG_TAG,"onPause/unregistering receiver");
        // 등록을 하면 멈추게된단
        if(myReceiver != null){
            activity.unregisterReceiver(myReceiver);
            myReceiver=null;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(LOG_TAG,"onstop()");
        if(myReceiver != null){
            activity.unregisterReceiver(myReceiver);
            myReceiver=null;
        }
        // Activity가 종료되기전에 저장한다
        //SharedPreferences 를 dSave 이름 기본모드로 설정한다
        SharedPreferences sharedPreferences = activity.getSharedPreferences("dSave",MODE_PRIVATE);
        // 저장을 하기 위해 editor를 이용하여 값을 저장시켜 준다
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //사용자가 입력한 저장할 데이터
        String text = textStep.getText().toString();
        // key , value 를 이용하여 저장하는 형태
        editor.putString("text",text);
        editor.commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(pedomterFrgment == null){

            pedomterFrgment = new PedomterFrgment();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        chronometer.stop();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            // 시작버튼
            case R.id.ivBtnBar :

                if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.N_MR1)
                    intent = new Intent(activity.getApplicationContext(),PedomterSensor.class);
                activity.startService(intent);
                Log.d(LOG_TAG,"onCreate/StartService");
                toastDisplay("만보기 시작");
                chronometer.setBase(SystemClock.elapsedRealtime());
                chronometer.start();
                onResume();
                break;
            // 중지 버튼

            case R.id.ivbtnTwo :
                boolean testData=false;
                testData=CheckInsertData();
                if(testData){
                    activity.stopService(intent);
                    toastDisplay("데이터가 존재합니다");
                }else{
                    insert(new PedColumnVO(calendar.get(Calendar.YEAR)
                            ,calendar.get(Calendar.MONTH+1),calendar.get(Calendar.DAY_OF_MONTH),ksm,ksm2));
                }

                toastDisplay("만보기 중단");
                chronometer.stop();
                //크로미터 정지
                chronometer.setBase(SystemClock.elapsedRealtime());
                // 센서값 안받기


                onStop();
                return;
        }

    }

    // BroadcastReceiver 내부클래스
    public class MyReceiver extends BroadcastReceiver {
        public static final String ACTION_RESTART_SERVICE="";
        static final  String LOG_TAG="MyReceiver";
        @Override
        public void onReceive(final Context context, Intent intent) {
            // SensorService 클래스에서 ksm 값을 받는다
            // 걸음값
            ksm = intent.getStringExtra("ksm");
            ksm2=intent.getStringExtra("ksm2");
            Log.d(LOG_TAG,"onReceive"+ksm);
            // 정지 -> 통계를 볼때 스레드 오류.
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while(!Thread.interrupted()){
//                        if(getActivity() == null){
//                            return;
//                        }
                        try {
                            Thread.sleep(1000);
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    textMinute.setText("진행시간");
                                    chronometerThread();
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }).start();

            // 값을 Toast로 확인한다
          //  Toast.makeText(context,""+ksm,Toast.LENGTH_LONG).show();
            // 값을 TextView 로 확인한다
            textStep.setText("Step:" +ksm);
            textKcal.setText(ksm2+" Kcal");
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    day_Graph.setProgress((int) Float.parseFloat(reading));
                    day_Kcal.setProgress((int) Float.parseFloat(reading));
                }
            });
            // 다이얼로그 창을 띄우기 -> 거기서 카운트가 10000이 된다면 스레드 센서 스탑


            if(intent.getAction().equals("ACTION.RESTART.PersistentService")){
                Intent i= new Intent(context,PedomterSensor.class);
                context.startService(i);
            }
            if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
                Intent i = new Intent(context,PedomterSensor.class);
                context.startService(i);
            }

        }

        //
        // 노티피케이션 을 터치했을때 그 해당된 앱으로 갈수있게(화면)
        // 스탑을 누르면 노티케이션 바도 없어지게.
    }
    //  스레드에 크로노미터 값을 넣는 함수
    public void chronometerThread() {
        // day_min 프로그래스 바에 크로미터 값을 넣는다

        progressThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(timeProgress[0] < 1){
                    chronometer.post(new Runnable() {
                        @Override
                        public void run() {
                            day_Min.setMax(1800);
                            day_Min.setProgress(timeProgress[0]);
                        }
                    });
                    timeProgress[0]++;
                    android.os.SystemClock.sleep(1000);
                }
            }
        });
        progressThread.start();
    }
    public void customActionBar(LayoutInflater inflater) {

        ActionBar actionBar=((MainActivity)getActivity()).getSupportActionBar();
        // Custom Actionbar 사용하기 위해 CustomEnabled을 true 시키고 필요 없는 것은 false 시킨다
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);            //액션바 아이콘을 업 네비게이션 형태로 표시합니다.
        actionBar.setDisplayShowTitleEnabled(false);        //액션바에 표시되는 제목의 표시유무를 설정합니다.
        actionBar.setDisplayShowHomeEnabled(false);//홈 아이콘을 숨김처리합니다.
        View actionbarlayout=inflater.inflate(R.layout.mainactionbar_layout,null);
        actionBar.setCustomView(actionbarlayout);
        //액션바 양쪽 공백 없애기
        Toolbar parent = (Toolbar)actionbarlayout.getParent();
        parent.setContentInsetsAbsolute(0,0);
        ActionBar.LayoutParams params=new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,ActionBar.LayoutParams.MATCH_PARENT);

        actionBar.setCustomView(actionbarlayout,params);

        ////end of 액션바 공백 없애기

        ImageButton main_btnAdd=actionbarlayout.findViewById(R.id.main_btnAdd);

//
//        main_btnAdd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                addArea();
//            }
//        }); //액션바위에 add버튼
    }

    public void insert(PedColumnVO pedColumnVO) {

        int year = pedColumnVO.getYear();
        int month = pedColumnVO.getMonth();
        int day = pedColumnVO.getDay();
        String uStep = pedColumnVO.getStep();
        String uKcal = pedColumnVO.getKcal();

        // DB 오픈
        db = DBHelper.getInstance(getActivity().getApplicationContext()).getWritableDatabase();
        db.execSQL("INSERT INTO PedTBL(year,month,day,step,kcal)" +
                "VALUES(" + year + "," + month + "," + day + ", '" + uStep + "', '" + uKcal + "');");
        // DB를 사용후 종료시키기
        db.close();
    }
    public ArrayList<String> getResult(PedColumnVO pedColumnVO){

        final    int year = pedColumnVO.getYear();
        int month = pedColumnVO.getMonth();
        int day = pedColumnVO.getDay();

        db = DBHelper.getInstance(getActivity().getApplicationContext()).getWritableDatabase();

        //cursor=db.rawQuery("SELECT step FROM PedTBL;",null);
        Cursor curDB = db.rawQuery("SELECT step FROM PedTBL WHERE year="+year+" AND month="+month+" AND day="+day+";",null);
        while (curDB.moveToNext()){
            //BARENTRY.add(curDB.getString(0));
            // BARENTRY.get(Integer.parseInt(curDB.getString(0)));
        }
        // 년 월 일에 대한 STEP의 걸음을 확인한다 ( 년 월 일에 있는 스탭을 가져 왔다 ~ PedTBL 으로부터  )
        return list;
    }
    // X축 년 월 일 구하기
    public ArrayList<String>getCalendar(PedColumnVO pedColumnVO){

        db = DBHelper.getInstance(getActivity().getApplicationContext()).getWritableDatabase();

        String step = pedColumnVO.getStep();
        String kcal = pedColumnVO.getKcal();

        // 걸음 , 칼로리에 대한 년 월 일을 확인한다 . ( 걸음 칼로리에 있는 년 월 일 을 가져 온다
        Cursor curCalendar = db.rawQuery("SELECT year ,month, day FROM PedTBL WHERE step="+step+" AND kcal="+kcal+";",null);
        while (curCalendar.moveToNext()){
            list.add(curCalendar.getString(0));
        }

        return listData;
    }


    private void toastDisplay(String s)
    {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }
}