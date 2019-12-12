package com.example.yunoi.cleaningmaster;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

import java.util.ArrayList;

import static android.app.Activity.DEFAULT_KEYS_DISABLE;

public class PedomterBar extends Fragment {

    BarChart chart;
    private View view;
    static ArrayList<BarEntry> BARENTRY ;
    static ArrayList<String> BarEntryLabels = new ArrayList<>();
    static BarDataSet Bardataset ;
    static BarData BARDATA ;
   // PedDBHelper pedDBHelper;
    SQLiteDatabase sqLiteDatabase;
    LineData lineData;
    LineDataSet lineDataSet = new LineDataSet(null,null);
    ArrayList<ILineDataSet> data = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.pedometerbar_xml,container,false);
        customActionBar(inflater);

        chart = view.findViewById(R.id.chart1);
        BARENTRY = new ArrayList<>();
        BarEntryLabels = new ArrayList<>();
        AddValuesToBARENTRY();
        AddValuesToBarEntryLabels();
        Bardataset = new BarDataSet(BARENTRY,"User Step");
        // 데이터를 차트로 채운다 BarData 객체를 초기화한다
        BARDATA = new BarData(BarEntryLabels, Bardataset);
        Bardataset.setColors(ColorTemplate.JOYFUL_COLORS);
        //Bardataset.setColor(ColorTemplate.JOYFUL_COLORS);
        // 데이터와 함께 막대 차트를 로드함
        chart.setData(BARDATA);
        // Y축으로 움직이는 속도 설정
        chart.animateY(3500);
        chart.setDescription("테스트하기");

        return view;

        //dataSets.add(Bardataset);
//        getIntent();
 //       pedDBHelper = new PedDBHelper(this);
//        sqLiteDatabase = pedDBHelper.getWritableDatabase();
//        lineDataSet.setValues(getDataValues());
//        lineDataSet.clear();
//        data.add(lineDataSet);
//        lineData = new LineData(data);
//        lineData.setData(lineData);
//        lineData.
    }

    private void customActionBar(LayoutInflater inflater) {

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

    public void AddValuesToBARENTRY(){

        // bar 생성 , 값 설정
        float test = 0.0f;

        BARENTRY.add(new BarEntry(test, 0));
        BARENTRY.add(new BarEntry(test, 1));
        BARENTRY.add(new BarEntry(null, 2));
        BARENTRY.add(new BarEntry(null, 3));
        BARENTRY.add(new BarEntry(null, 4));
        BARENTRY.add(new BarEntry(null, 5));
        BARENTRY.add(new BarEntry(null, 6));

    }
    public void AddValuesToBarEntryLabels(){
        // bar Label 설정

        BarEntryLabels.add("월요일");
        BarEntryLabels.add("화요일");
        BarEntryLabels.add("수요일");
        BarEntryLabels.add("목요일");
        BarEntryLabels.add("금요일");
        BarEntryLabels.add("토요일");
        BarEntryLabels.add("일요일");
    }
}

