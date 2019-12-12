package com.example.yunoi.cleaningmaster;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;

import java.util.ArrayList;

public class TodolistAdapter extends RecyclerView.Adapter<TodolistAdapter.CustomViewHolder> {

    int layout;
    ArrayList<TodolistVo> list;

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
    public void onBindViewHolder(@NonNull TodolistAdapter.CustomViewHolder customViewHolder, int position) {

        customViewHolder.todolist_text.setText(list.get(position).getTodolist_text());
        customViewHolder.swipe_sample1.setShowMode(SwipeLayout.ShowMode.LayDown);
        customViewHolder.swipe_sample1.getSurfaceView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        private ImageButton todolist_alram;
        private TextView todolist_text;
        private Switch todolist_switch;
        private SwipeLayout swipe_sample1;
        private boolean btnCheck = false;
        private TextView txtDayCheck;


        public CustomViewHolder(@NonNull final View itemView) {
            super(itemView);


            todolist_alram = itemView.findViewById(R.id.todolist_alram);
            todolist_text = itemView.findViewById(R.id.todolist_text);
            todolist_switch = itemView.findViewById(R.id.todolist_switch);
            swipe_sample1 = itemView.findViewById(R.id.swipe_sample1);

            todolist_alram.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final View dialogView = View.inflate(itemView.getContext(), R.layout.dialog_add_notify, null);
                    final AlertDialog.Builder dlg = new AlertDialog.Builder(itemView.getContext());
                    Button btnMonday = itemView.findViewById(R.id.btnMonday);
                    Button btnTuesday = itemView.findViewById(R.id.btnTuesday);
                    Button btnWednesday = itemView.findViewById(R.id.btnWednesday);
                    Button btnThursday = itemView.findViewById(R.id.btnThursday);
                    Button btnFriday = itemView.findViewById(R.id.btnFriday);
                    Button btnSaturday = itemView.findViewById(R.id.btnSaturday);
                    Button btnSunday = itemView.findViewById(R.id.btnSunday);
                    txtDayCheck = itemView.findViewById(R.id.txtDayCheck);

                    dlg.setView(dialogView);

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

        private void btnBackGroundChange(Button btn) {
            if (btnCheck == false) {
                btn.setBackground(ContextCompat.getDrawable(itemView.getContext(), R.drawable.btndayofweek_btnon));
                txtDayCheck.setText("매주 " + btn.getText().toString().trim() + "요일에 알람이 울립니다.");
                btnCheck = true;
            } else {
                btn.setBackground(ContextCompat.getDrawable(itemView.getContext(), R.drawable.btndayofweek_btnoff));
                txtDayCheck.setText("매일 알림");
                btnCheck = false;
            }
        }
    }
}
