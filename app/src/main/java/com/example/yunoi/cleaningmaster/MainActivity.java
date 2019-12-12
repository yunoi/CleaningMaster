package com.example.yunoi.cleaningmaster;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.stetho.Stetho;

public class MainActivity extends AppCompatActivity {

    private CoordinatorLayout coordinatorLayout;
    private BottomNavigationView bottomMenu;
    private FragmentManager fragmentManager;
    private FragmentTransaction ft;
    private Fragment pedomterFrgment;
    private Fragment pedomterBar;

    private SettingFragment settingFragment;
    private Fragment mainFragment;
    private static final String TAG = "MainActivity";
    //191212 am 11:20 도움말 이동에 관련된 SharedPreferences by 재훈
    SharedPreferences passTutorial;
    int tutorialState;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Stetho.initializeWithDefaults(this);

        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        bottomMenu = findViewById(R.id.bottomMenu);
        settingFragment = new SettingFragment();
        mainFragment = new MainFragment();
        pedomterFrgment=new PedomterFrgment();
        pedomterBar=new PedomterBar();

        //191212 am 11:20 도움말 안보기 설정이 아닐시 자동으로 도움말로 이동 by 재훈
//        passTutorial = getSharedPreferences("change",MODE_PRIVATE);
//        tutorialState = passTutorial.getInt("First",0);
//        if(tutorialState !=1){
//            Intent intent = new Intent(MainActivity.this,TutorialGuideActivity.class);
//            startActivity(intent);
//        }

        Log.d(TAG, "test입니다.");

        // bottomMenu를 변경했을 때 그것을 감지하여 해당된 프래그먼트를 세팅해주는 리스너
        bottomMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_1:
                        setOnChangeFragment(0);
                        break;
                    case R.id.action_2:
                        setOnChangeFragment(1);
                        break;
                    case R.id.action_3:
                        setOnChangeFragment(2);
                        break;
                    case R.id.action_4:
                        setOnChangeFragment(3);
                        break;
                }
                return true;
            }
        });
        setOnChangeFragment(0);
    }

    private void setOnChangeFragment(int i) {
        // 화면 전환 위해서는 프래그먼트 매니저 필요
        fragmentManager = getSupportFragmentManager();
        // 프래그먼트 매니저의 권한을 받아서 화면을 바꾸는 역할의 트랜젝션 필요
        ft = fragmentManager.beginTransaction();
        switch(i){
            case 0:
                ft.replace(R.id.coordinatorLayout, mainFragment);
                ft.commit();
                break;
                //성민이꺼
            case 1:ft.replace(R.id.coordinatorLayout,pedomterFrgment);
                   ft.commit();

                break;
            case 2:

                break;
            case 3:

                break;

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, 1, 0, "프로필");
        menu.add(0, 2, 0, "통계");
        menu.add(0, 3, 0, "설정");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 3:
                fragmentManager = getSupportFragmentManager();
                ft = fragmentManager.beginTransaction();
                toastDisplay("옵션메뉴테스트");
                Log.d("MainActivity", "설정");
                ft.replace(R.id.coordinatorLayout, settingFragment);
                ft.commit();
                return true;

            case 2:
                fragmentManager=getSupportFragmentManager();
                ft=fragmentManager.beginTransaction();
                toastDisplay("통계 테스트");
                Log.d("MainActivity","통계");
                ft.replace(R.id.coordinatorLayout,pedomterBar);
                ft.commit();
                return true;
        }

        return false;
    }

    public void toastDisplay(String s) {
        Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
    }


}
