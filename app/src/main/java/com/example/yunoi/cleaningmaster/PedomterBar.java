package com.example.yunoi.cleaningmaster;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static android.app.Activity.DEFAULT_KEYS_DISABLE;

public class PedomterBar extends Fragment {
    BarChart chart;
    private SimpleDateFormat cFormat = new SimpleDateFormat("MM/dd");
    private View view;
    private Calendar calendar = Calendar.getInstance();
    private ArrayList<BarEntry> BARENTRY;
    private ArrayList<String> BarEntryLabels = new ArrayList<>();
    private BarDataSet Bardataset;
    private BarData BARDATA;
    private Activity activity;
    private Context mContext;

    // PedDBHelper pedDBHelper;
    private SQLiteDatabase sqLiteDatabase;
    private DBHelper dbHelper;

    String day;
    Intent intent;
    SQLiteDatabase db;
    ArrayList<PedColumnVO> list2 = new ArrayList<>();
    ArrayList<Integer> dayList = new ArrayList<>();
    ArrayList<String> stepList = new ArrayList<>();


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.pedometerbar_xml, container, false);
        customActionBar(inflater);
        chart = view.findViewById(R.id.chart1);
        BARENTRY = new ArrayList<>();
        BarEntryLabels = new ArrayList<>();
        AddValuesToBARENTRY();
        AddValuesToBarEntryLabels();
        Bardataset = new BarDataSet(BARENTRY, "걸음");
        // 데이터를 차트로 채운다 BarData 객체를 초기화한다
        BARDATA = new BarData(BarEntryLabels, Bardataset);
        Bardataset.setColors(ColorTemplate.JOYFUL_COLORS);
        //Bardataset.setColor(ColorTemplate.JOYFUL_COLORS);
        // 데이터와 함께 막대 차트를 로드함
        chart.setData(BARDATA);
        // Y축으로 움직이는 속도 설정
        chart.animateY(3500);
        chart.setDescription("테스트하기");
        intent = new Intent();
        activity.getIntent();

        return view;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        if (context instanceof Activity) {
            activity = (Activity) context;
        }
    }

    private void customActionBar(LayoutInflater inflater) {

        ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();

        // Custom Actionbar 사용하기 위해 CustomEnabled을 true 시키고 필요 없는 것은 false 시킨다
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);            //액션바 아이콘을 업 네비게이션 형태로 표시합니다.
        actionBar.setDisplayShowTitleEnabled(false);        //액션바에 표시되는 제목의 표시유무를 설정합니다.
        actionBar.setDisplayShowHomeEnabled(false);//홈 아이콘을 숨김처리합니다.

        View actionbarlayout = inflater.inflate(R.layout.mainactionbar_layout, null);
        actionBar.setCustomView(actionbarlayout);
        //액션바 양쪽 공백 없애기
        Toolbar parent = (Toolbar) actionbarlayout.getParent();
        parent.setContentInsetsAbsolute(0, 0);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);

        actionBar.setCustomView(actionbarlayout, params);

        ////end of 액션바 공백 없애기

        ImageButton main_btnAdd = actionbarlayout.findViewById(R.id.main_btnAdd);

//
//        main_btnAdd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                addArea();
//            }
//        }); //액션바위에 add버튼
    }

    public void AddValuesToBARENTRY() {
        int cDay = calendar.get(Calendar.DAY_OF_MONTH);
        int bDay = cDay - 7;
        db = DBHelper.getInstance(getActivity().getApplicationContext()).getWritableDatabase();
        ;
        //    SELECT * FROM test WHERE date BETWEEN "2011-01-11" AND "2011-8-11"
        Cursor curDB2 = db.rawQuery("SELECT day , step FROM PedTBL WHERE day BETWEEN " + bDay + " AND " + cDay + "; ", null);
        //  Cursor curDB = database.rawQuery("SELECT day, step FROM PedTBL WHERE day = 16 <= 날짜 AND 날짜 >16-7;",null);
        while (curDB2.moveToNext()) {
            // curDB에 담겨진 변수를 객체화 시켜서 list2에 담는다
            dayList.add(Integer.parseInt(curDB2.getString(0)));
            stepList.add(curDB2.getString(1));
        }
        // intent = new Intent(activity.getApplicationContext(),PedomterSensor.class);
//        Intent intent = new Intent(activity.MainActivity.this, PedomterBar.class);

        intent.putIntegerArrayListExtra("dayList", dayList);
        intent.putStringArrayListExtra("stepList", stepList);
        startActivity(intent);
        Log.d("test", String.valueOf(list2));

        //Intent intent = getIntent() => intent 로 부터 값을 받는다
        Intent intent = activity.getIntent();

        stepList = intent.getStringArrayListExtra("stepList");

        for (int i = 0; i < stepList.size(); i++) {
            BARENTRY.add(new BarEntry(Float.parseFloat(stepList.get(i)), i));
            // bar 생성 , 값 설정
        }
    }
        public void AddValuesToBarEntryLabels(){
            // bar Label 설정 X축
            Intent intent = activity.getIntent();
            ArrayList<Integer> dayList = new ArrayList<>();
            dayList=intent.getIntegerArrayListExtra("dayList");
            for(int i = 0 ; i < dayList.size() ; i ++ ){
                BarEntryLabels.add(String.valueOf(dayList.get(i)));


            }
        }
    }



