package com.hypernym.evaconnect.view.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.onesignal.OneSignal;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        init();
        initOneSignal();
    }

    private void init() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (LoginUtils.isUserLogin()) {
                    Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        }, 3000);
    }

    public void initOneSignal() {

        OneSignal.startInit(this)
                //.inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.None)
                .init();

        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {
                Log.e("debug", "User:" + userId);
                if (registrationId != null)
                    Log.e("debug", "registrationId:" + registrationId);
            }
        });
    }
}
