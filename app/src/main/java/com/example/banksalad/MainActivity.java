package com.example.banksalad;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.banksalad.fragment.fragWatch;
import com.example.banksalad.fragment.fragPlan;
import com.example.banksalad.fragment.fragUser;
import com.example.banksalad.fragment.tabFragment1;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import static androidx.constraintlayout.widget.StateSet.TAG;


public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    Menu menu;

    Fragment tabFragment1;
    Fragment fragUser;
    Fragment fragPlan;
    Fragment fragWatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabFragment1 = new tabFragment1();
        fragUser = new fragUser();
        fragPlan = new fragPlan();
        fragWatch = new fragWatch();

        Intent intent = getIntent();
        final String userName = intent.getStringExtra("userName");
        final String userBirth = intent.getStringExtra("userBirth");
        final String userHeight = intent.getStringExtra("userHeight");
        final String userWeight = intent.getStringExtra("userWeight");

        bottomNavigationView = findViewById(R.id.navigation);

        //첫 화면 띄우기
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,tabFragment1).commitAllowingStateLoss();

        bottomNavigationView = findViewById(R.id.navigation);

        //case 함수를 통해 클릭 받을 때마다 화면 변경하기
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.cal :
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,new tabFragment1()).commit();
                        break;
                    case R.id.user:
                        Bundle bundle = new Bundle();
                        bundle.putString("userName",userName);
                        bundle.putString("userBirth",userBirth);
                        bundle.putString("userHeight",userHeight);
                        bundle.putString("userWeight",userWeight);
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        Fragment fragment1 = new fragUser();
                        fragment1.setArguments(bundle);
                        transaction.replace(R.id.frame_container, fragment1);
                        transaction.commit();
                        break;
                    case R.id.plan:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,new fragPlan()).commit();
                        break;
                    case R.id.watch:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,new fragWatch()).commit();
                        break;
                }
                return true;
            }
        });

    }

    public void mOnClick(View v){
        Intent intent = new Intent(getApplicationContext(),addList.class);
        startActivity(intent);
    }
}
