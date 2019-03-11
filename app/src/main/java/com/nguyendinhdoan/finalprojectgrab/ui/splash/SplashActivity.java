package com.nguyendinhdoan.finalprojectgrab.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.nguyendinhdoan.finalprojectgrab.R;
import com.nguyendinhdoan.finalprojectgrab.ui.login.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // delay 2(s)
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                launchLogin();
            }
        }, SPLASH_TIME_OUT);
    }

    private void launchLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
