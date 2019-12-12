package com.example.yunoi.cleaningmaster;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;

import java.util.ArrayList;

import lib.mozidev.me.extextview.ExTextView;
import lib.mozidev.me.extextview.StrikeThroughPainting;

import static android.support.v4.content.ContextCompat.startActivity;

public class TodolistAdapter extends RecyclerView.Adapter<TodolistAdapter.CustomViewHolder> {

    private int layout;
    private ArrayList<TodolistVo> list;
    private int checkcount=0;
    private static final String TAG="확인";

    //생성자
    public TodolistAdapter(int layout, ArrayList<TodolistVo> list) {
        this.layout = layout;
        this.list = list;
    }

    @NonNull
    @Override
    public TodolistAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(layout, viewGroup, false);
        CustomViewHolder customViewHolder = new CustomViewHolder(view);


        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final TodolistAdapter.CustomViewHolder customViewHolder, final int position) {

        customViewHolder.todolist_text.setText(list.get(position).getTodolist_text());
        final int taskCount=list.get(position).getTaskcount();

        //취소 버튼 액션
        customViewHolder.swipe_sample1.setShowMode(SwipeLayout.ShowMode.LayDown);
        customViewHolder.swipe_sample1.findViewById(R.id.delete_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                //취소 알런트
                final View alertDialogView=View.inflate(v.getContext(),R.layout.dialog_delete_layout,null);
                AlertDialog.Builder builder=new AlertDialog.Builder(v.getContext(),R.style.MyDialogTheme);
                builder.setView(alertDialogView);
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        list.remove(position);
                        notifyDataSetChanged();
                        Snackbar snackbar=Snackbar.make(customViewHolder.todo_linearLayout,"삭제되었습니다!",Snackbar.LENGTH_SHORT);

                        snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                        View snackbarView=snackbar.getView();
                        TextView tv=(TextView)snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                        tv.setTextColor(Color.WHITE);

                        snackbarView.setBackgroundColor(Color.parseColor("#024873"));

                        snackbar.show();

                    }
                });
                builder.setNegativeButton("취소",null);
                builder.show();
            }
        });



    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        private ImageButton todolist_alram;
        private ExTextView todolist_text;
        private Switch todolist_switch;
        private SwipeLayout swipe_sample1;
        private LinearLayout todo_linearLayout;
        private CheckBox todolist_checkBox;
        StrikeThroughPainting strikeThroughPainting;
        private boolean btnCheck = false;
        private TextView txtDayCheck;

        public CustomViewHolder(@NonNull final View itemView) {
            super(itemView);

            todolist_alram=itemView.findViewById(R.id.todolist_alram);
            todolist_text=itemView.findViewById(R.id.todolist_text);
            todolist_switch=itemView.findViewById(R.id.todolist_switch);
            swipe_sample1=itemView.findViewById(R.id.swipe_sample1);
            todo_linearLayout=itemView.findViewById(R.id.todo_linearLayout);
            todolist_checkBox=itemView.findViewById(R.id.todolist_checkBox);
            strikeThroughPainting = new StrikeThroughPainting(todolist_text);


            todolist_checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                    if (isChecked==true){
                        strikeThroughPainting.color(Color.rgb(2, 72, 112))
                                .strokeWidth(4).totalTime(10_0L).strikeThrough();
                        checkcount++;//체크 카운트

                        //체크한 카운트가 만든 리스트와 맞을때
                        if (checkcount==TodolistFragment.taskcount){

                            Intent intent=new Intent(itemView.getContext(),ExpShowActivity.class);
                            startActivity(itemView.getContext(),intent,null);

                        }




                    }else {
                        checkcount--;
                        strikeThroughPainting.clearStrikeThrough();
                    }

                    Log.d(TAG,"체크 카운트 확인 : "+checkcount+" / 총 리스트 개수 : "+String.valueOf(TodolistFragment.taskcount));

                }
            });


            todolist_alram = itemView.findViewById(R.id.todolist_alram);

            todolist_alram.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final View dialogView = View.inflate(itemView.getContext(), R.layout.dialog_add_notify, null);
                    final AlertDialog.Builder dlg = new AlertDialog.Builder(itemView.getContext());
                    dlg.setView(dialogView);

                    final Button btnMonday = dialogView.findViewById(R.id.btnMonday);
                    final Button btnTuesday = dialogView.findViewById(R.id.btnTuesday);
                    final Button btnWednesday = dialogView.findViewById(R.id.btnWednesday);
                    final Button btnThursday = dialogView.findViewById(R.id.btnThursday);
                    final Button btnFriday = dialogView.findViewById(R.id.btnFriday);
                    final Button btnSaturday = dialogView.findViewById(R.id.btnSaturday);
                    final Button btnSunday = dialogView.findViewById(R.id.btnSunday);
                    txtDayCheck = dialogView.findViewById(R.id.txtDayCheck);

                    btnMonday.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            btnBackGroundChange(btnMonday);
                        }
                    });

                    dlg.setPositiveButton("확인", null);
                    dlg.setNegativeButton("취소",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    Toast.makeText(itemView.getContext(), "취소되었습니다.", Toast.LENGTH_SHORT).show();
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

                                    Toast.makeText(itemView.getContext(), "알림 입력", Toast.LENGTH_SHORT).show();

                                    alertDialog.dismiss();
                                }
                            });
                        } // end of onShow
                    });
                    alertDialog.show();
                }
            });
        }
        //요일 버튼 이벤트
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        private void btnBackGroundChange(Button btn) {
            if(!btnCheck){
                btn.setBackground(ContextCompat.getDrawable(itemView.getContext(),R.drawable.btndayofweek_btnon));
                txtDayCheck.setText("매주 "+btn.getText().toString().trim()+"요일에 알람이 울립니다.");
                btnCheck=true;
            }else{
                btn.setBackground(ContextCompat.getDrawable(itemView.getContext(),R.drawable.btndayofweek_btnoff));
                txtDayCheck.setText("매일 알림");
                btnCheck=false;
            }
        }

    } // end of customViewHolder class
}
