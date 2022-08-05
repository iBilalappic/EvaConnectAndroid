package com.hypernym.evaconnect.view.ui.activities;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.onesignal.OneSignal;

public class SplashActivity extends BaseActivity {
    public static final int RequestPermissionCode = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if(!Checkpermission())
        {
            requestpermission();
        }
        else
        {
            init();

        }
        initOneSignal();
    }
    public boolean Checkpermission() {

        int ExternalWrite = ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE);
        int ExternalReadResult = ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE);
        int CameraResult = ContextCompat.checkSelfPermission(this, CAMERA);
        int locationResult = ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION);
        return        ExternalWrite == PackageManager.PERMISSION_GRANTED &&  ExternalReadResult == PackageManager.PERMISSION_GRANTED &&
                CameraResult == PackageManager.PERMISSION_GRANTED &&
                locationResult== PackageManager.PERMISSION_GRANTED;
    }

    private void requestpermission() {
        ActivityCompat.requestPermissions(this, new String[]
                {WRITE_EXTERNAL_STORAGE,
                        READ_EXTERNAL_STORAGE,
                        CAMERA,
                        ACCESS_FINE_LOCATION
                }, RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(permsRequestCode, permissions, grantResults);
        init();
    }

    private void init() {
        new Handler().postDelayed(() -> {
            if (LoginUtils.isUserLogin()) {
                Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                if (getIntent().getExtras() != null) {
                    intent.putExtra("chat_room_id", getIntent().getStringExtra("chat_room_id"));
                }
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }

        }, 3000);
    }

    public void initOneSignal() {

        OneSignal.startInit(this)
                //.inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.None)
                .init();

        OneSignal.idsAvailable((userId, registrationId) -> {
            Log.e("debug", "User:" + userId);
            if (registrationId != null)
                Log.e("debug", "registrationId:" + registrationId);
        });
    }
}
