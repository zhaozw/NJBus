package com.renyu.nj_tran.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.renyu.nj_tran.R;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by renyu on 15/9/9.
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                System.out.println("jpushId:" + JPushInterface.getRegistrationID(SplashActivity.this));
                Intent intent = new Intent(SplashActivity.this, MyLocationActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2500);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
