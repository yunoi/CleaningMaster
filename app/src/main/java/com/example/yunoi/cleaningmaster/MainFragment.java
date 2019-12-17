package com.example.yunoi.cleaningmaster;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;

import java.util.ArrayList;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class MainFragment extends Fragment implements LoadAlarmsReceiver.OnAlarmsLoadedListener {

    private View view;
    private ListView listView;
    private ArrayList<String> list = new ArrayList<>();
    private SQLiteDatabase db;
    private DBHelper dbHelper;
    private MainAdapter adapter;
    private EditText alerEdt; // 청소구역추가 다이얼로그 내부 변수
    private int checkedPosition; // 리스트뷰의 포지션을 가져온다.
    private static final String TAG = "MainFragment";
    private LoadAlarmsReceiver mReceiver;
    private Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mReceiver = new LoadAlarmsReceiver(this);
        Log.i(getClass().getSimpleName(), "onCreate ...");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_fragment, container, false);
        customActionBar(inflater);

        listView = view.findViewById(R.id.listView);
        list = getTotalArea();
        final MainAdapter adapter = new MainAdapter(getActivity().getApplicationContext(), R.layout.main_list_view_holder, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "position = "+position);
                checkedPosition = listView.getCheckedItemPosition(); // 리스트뷰의 포지션을 가져온다.
                Log.d(TAG, "checkedPosition = "+checkedPosition);
            }
        });
        return view;
    }

    private void addArea() {
        final View dialogView = View.inflate(getActivity().getApplicationContext(), R.layout.dialog_add_room, null);
        final AlertDialog.Builder dlg = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);

        dlg.setView(dialogView);
        ImageView imageView =
                dialogView.findViewById(R.id.imageView);
        TextView alertTxt1 =
                dialogView.findViewById(R.id.alertTxt1);
        alerEdt = dialogView.findViewById(R.id.alerEdt);

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
                        Log.d(TAG, str1);
                        if (str1.equals("")) {
                            toastDisplay("청소구역을 입력해 주세요!");
                        } else {
                            if (preventDuplicateArea(str1)) {
                                insertMainCleaningArea(str1);
                                list.add(str1);
                                toastDisplay(str1+"이(가) 추가되었습니다.");
                                alertDialog.dismiss();
                            }
                        }
                    }
                });
            } // end of onShow
        });
        alertDialog.show();

    } // end of addArea

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


        main_btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addArea();
            }
        }); //액션바위에 add버튼


    }

    // 청소구역 입력 (insert)
    public void insertMainCleaningArea(String area) {
        db = DBHelper.getInstance(getActivity().getApplicationContext()).getWritableDatabase();
        db.execSQL("INSERT INTO areaTBL ( area )" +
                "VALUES ('"+ area +"');");
    }

    // 전체 청소구역 불러오기 (select)
    public ArrayList<String> getTotalArea() {
        db = DBHelper.getInstance(getActivity().getApplicationContext()).getReadableDatabase();
        Cursor cursor;
        cursor = db.rawQuery("SELECT DISTINCT area FROM areaTBL;", null);
        list.clear();
        while (cursor.moveToNext()) {
            list.add(cursor.getString(0));
        }
        cursor.close();
        return list;
    }

    // 청소구역 중복검사
    public boolean preventDuplicateArea(String area) {
        db = DBHelper.getInstance(getActivity().getApplicationContext()).getReadableDatabase();
        Cursor cursor;
        cursor = db.rawQuery("SELECT area FROM areaTBL WHERE area = '" + area + "' limit 1;", null);
//        Log.d(TAG, cursor.getString(0));
        while(cursor.moveToNext()){
            if (!cursor.getString(0).equals("")) {
                toastDisplay("이미 존재하는 청소구역입니다!");
                return false;
            }
        }
        cursor.close();
        return true;
    }

    //청소 구역 삭제하기 1212 pm 5:07 by 채현 -> 1213 pm 9:42 by 윤해
    public void deleteArea(String groupName) {
        db = DBHelper.getInstance(getActivity().getApplicationContext()).getWritableDatabase();
        db.execSQL("DELETE FROM areaTBL WHERE area='" + groupName + "';");
        Log.d(TAG, "cleaningTBL에서 청소구역 삭제됨");
    }

    // 청소 구역 수정 (upadate)
    public void updateArea() {
        final View dialogView = View.inflate(getActivity().getApplicationContext(), R.layout.dialog_add_room, null);
        final AlertDialog.Builder dlg = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);

        dlg.setView(dialogView);
        ImageView imageView =
                dialogView.findViewById(R.id.imageView);
        TextView alertTxt1 =
                dialogView.findViewById(R.id.alertTxt1);
        alerEdt = dialogView.findViewById(R.id.alerEdt);
        alertTxt1.setText("변경하실 구역명을 작성해 주세요");
        final String curArea = list.get(checkedPosition);
        alerEdt.setText(list.get(checkedPosition));
        Log.d(TAG, "curArea + alerEdt.setText의 포지션 = "+checkedPosition);

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
                        String newArea = alerEdt.getText().toString(); // 내용을 꼭 입력하도록 막아놓기
                        if (newArea.equals("")) {
                            toastDisplay("청소구역을 입력해 주세요!");
                        } else {
                            Log.d(TAG, "추가된 리스트: " + newArea);
                            if (preventDuplicateArea(newArea)) {
                                db = DBHelper.getInstance(getActivity().getApplicationContext()).getWritableDatabase();
                                if (alerEdt.getText().toString() != "") {
                                    db.execSQL("UPDATE areaTBL SET area = '"
                                            + newArea + "' WHERE area = '"
                                            + curArea + "';");
                                }
                                list.remove(checkedPosition);
                                list.add(newArea);
                                Log.d(TAG, "기존 리스트: " + curArea);
                                toastDisplay("수정되었습니다.");
                                alertDialog.dismiss();
                            }


                        }
                    }
                });
            } // end of onShow
        });
        alertDialog.show();

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
            layoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = layoutInflater.inflate(layout, null);
            }

            final LinearLayout linearLayout = convertView.findViewById(R.id.linearLayout);
            TextView tvCleaning = convertView.findViewById(R.id.tvCleaning);
            ImageView ivAddTask = convertView.findViewById(R.id.ivAddTask);
            tvCleaning.setTag(convertView);
            tvCleaning.setText(list.get(position));
            //191212 pm 3:51 SwipeLayout 추가 (DeleteButton 기능)
            final String groupText = tvCleaning.getText().toString();

            SwipeLayout swipeLayout = convertView.findViewById(R.id.main_swipeLayout);
            swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
            swipeLayout.findViewById(R.id.main_delete_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //취소 알런트
                    final View alertDialogView = View.inflate(v.getContext(), R.layout.dialog_maindelete_layout, null);
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);
                    builder.setView(alertDialogView);
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            list.remove(position);
                            notifyDataSetChanged();

                            deleteArea(groupText); // 청소구역 db 삭제

                            Snackbar snackbar = Snackbar.make(linearLayout, "삭제되었습니다!", Snackbar.LENGTH_SHORT);

                            snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                            View snackbarView = snackbar.getView();
                            TextView tv = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                            tv.setTextColor(Color.WHITE);

                            snackbarView.setBackgroundColor(Color.parseColor("#024873"));
                            snackbar.show();


                        }
                    });
                    builder.setNegativeButton("취소", null);
                    builder.show();
                }
            });

            //191212 am 09:51 linearlayout 클릭 리스너 추가 by 채현

            ivAddTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //todolist 화면 전환시 데이터 전달
                    Fragment fragment = new TodolistFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("groupText", groupText);
                    fragment.setArguments(bundle);

                    //todolist 화면 전환
                    FragmentManager fragmentManager = ((MainActivity) getActivity()).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.coordinatorLayout, fragment).commit();

                }
            });

            // 청소구역 수정
            tvCleaning.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final View dialogView = View.inflate(getActivity().getApplicationContext(), R.layout.dialog_add_room, null);
                    final AlertDialog.Builder dlg = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);

                    dlg.setView(dialogView);
                    ImageView imageView =
                            dialogView.findViewById(R.id.imageView);
                    TextView alertTxt1 =
                            dialogView.findViewById(R.id.alertTxt1);
                    alerEdt = dialogView.findViewById(R.id.alerEdt);
                    alertTxt1.setText("변경하실 구역명을 작성해 주세요");
                    final String curArea = list.get(position);
                    alerEdt.setText(list.get(position));
                    Log.d(TAG, "curArea + alerEdt.setText의 포지션 = "+position);

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
                                    String newArea = alerEdt.getText().toString(); // 내용을 꼭 입력하도록 막아놓기
                                    if (newArea.equals("")) {
                                        toastDisplay("청소구역을 입력해 주세요!");
                                    } else {
                                        Log.d(TAG, "추가된 리스트: " + newArea);
                                        if (preventDuplicateArea(newArea)) {
                                            db = DBHelper.getInstance(getActivity().getApplicationContext()).getWritableDatabase();
                                            if (alerEdt.getText().toString() != "") {
                                                db.execSQL("UPDATE areaTBL SET area = '"
                                                        + newArea + "' WHERE area = '"
                                                        + curArea + "';");
                                            }
                                            list.remove(position);
                                            list.add(newArea);
                                            Log.d(TAG, "기존 리스트: " + curArea);
                                            toastDisplay("수정되었습니다.");
                                            alertDialog.dismiss();
                                        }


                                    }
                                }
                            });
                        } // end of onShow
                    });
                    alertDialog.show();
                }
            });
            return convertView;
        }

    }// end of MainAdapter

    public void toastDisplay(String s) {
        Toast.makeText(getActivity().getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        final IntentFilter filter = new IntentFilter(LoadAlarmsService.ACTION_COMPLETE);
        LocalBroadcastManager.getInstance(context).registerReceiver(mReceiver, filter);
        LoadAlarmsService.launchLoadAlarmsService(context);
        Log.d(TAG, "onStart");
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(context).unregisterReceiver(mReceiver);
        Log.d(TAG, "onStop");
    }

    @Override
    public void onAlarmsLoaded(ArrayList<TodolistVo> alarms) {
        TodolistAdapter todolistAdapter = new TodolistAdapter();
        for (TodolistVo list : alarms) {
            Log.d(TAG, "onAlarmsLoaded. list: "+list.toString());
        }
        todolistAdapter.setAlarms(alarms);
        Log.d(TAG, "onAlarmsLoaded");
    }

}
