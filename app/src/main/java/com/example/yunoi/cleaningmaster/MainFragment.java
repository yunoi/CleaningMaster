package com.example.yunoi.cleaningmaster;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class MainFragment extends Fragment {

    private View view;
    private ListView listView;
    private ArrayList<String> list = new ArrayList<>();
    private SQLiteDatabase db;
    private DBHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_fragment,container,false);
        customActionBar(inflater);

        listView = view.findViewById(R.id.listView);
        list = getTotalArea();
        MainAdapter adapter = new MainAdapter(getActivity().getApplicationContext(), R.layout.main_list_view_holder, list);
        listView.setAdapter(adapter);

        return view;
    }

    private void addArea() {
        final View dialogView = View.inflate(getActivity().getApplicationContext(), R.layout.dialog_add_room, null);
        final AlertDialog.Builder dlg = new AlertDialog.Builder(getActivity(),R.style.MyDialogTheme);

        dlg.setView(dialogView);
        ImageView imageView =
                dialogView.findViewById(R.id.imageView);
        TextView alertTxt1 =
                dialogView.findViewById(R.id.alertTxt1);
        final EditText alerEdt = dialogView.findViewById(R.id.alerEdt);

        dlg.setPositiveButton("확인", null);
        dlg.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        toastDisplay("취소되었습니다.");
                    }
                });
        final AlertDialog alertDialog = dlg.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String str1 = alerEdt.getText().toString(); // 내용을 꼭 입력하도록 막아놓기
                        if(str1.equals("")){
                            toastDisplay("청소구역을 입력해 주세요!");
                        } else{
                            list.add(str1);
                            insertArea(new NotifyVO(0,0,0,0,0,str1, null, null, null));
                            alertDialog.dismiss();
                        }
                    }
                });
            } // end of onShow
        });
        alertDialog.show();
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


        main_btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addArea();
            }
        }); //액션바위에 add버튼



    }

    public void toastDisplay(String s) {
        Toast.makeText(getActivity().getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }

    // 청소구역 입력 (insert)
    public void insertArea(NotifyVO notifyVO){
        db = DBHelper.getInstance(getActivity().getApplicationContext()).getWritableDatabase();
        int year = notifyVO.getYear();
        int month = notifyVO.getMonth();
        int day = notifyVO.getDay();
        int hour = notifyVO.getHour();
        int minute = notifyVO.getMinute();
        String area = notifyVO.getArea();
        String task = notifyVO.getTask();
        String alarmSet = notifyVO.getAlarmSet();
        String loop = notifyVO.getLoop();
        db.execSQL("INSERT INTO notifyTBL (year, month, day, hour, minute, area, task, alarmSet, loop )" +
                "VALUES ("+year+","+ month+","+day+","+hour+"," +minute+", '"+ area +"', '" + task +"','" + alarmSet +"','"+loop+"');");

    }
    // 전체 청소구역 불러오기 (select)
    public ArrayList<String> getTotalArea(){
        db = DBHelper.getInstance(getActivity().getApplicationContext()).getReadableDatabase();
        Cursor cursor;
        cursor = db.rawQuery("SELECT area FROM notifyTBL;", null);
        while(cursor.moveToNext()){
            list.add(cursor.getString(0));
        }
        cursor.close();
        return list;
    }

    class MainAdapter extends BaseAdapter {
        private Context context;
        private int layout;
        private ArrayList<String> list;
        private LayoutInflater layoutInflater;

        public MainAdapter(Context context, int layout, ArrayList<String> list) {
            this.context = context;
            this.layout = layout;
            this.list = list;
            layoutInflater = (LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = layoutInflater.inflate(layout, null);
            }
            LinearLayout linearLayout = convertView.findViewById(R.id.linearLayout);
            TextView tvCleaning = convertView.findViewById(R.id.tvCleaning);
            ImageView ivAddTask = convertView.findViewById(R.id.ivAddTask);

            tvCleaning.setText(list.get(position));
            return convertView;
        }
    }   // end of MainAdapter
}
