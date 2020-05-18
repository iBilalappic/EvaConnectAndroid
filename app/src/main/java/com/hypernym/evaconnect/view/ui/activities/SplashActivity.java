package com.hypernym.evaconnect.view.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.onesignal.OneSignal;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static com.hypernym.evaconnect.view.ui.activities.SignupDetailsActivity.RequestPermissionCode;

public class SplashActivity extends AppCompatActivity {

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

        int ExternalReadResult = ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE);
        int CameraResult = ContextCompat.checkSelfPermission(this, CAMERA);
        int locationResult = ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION);
        return          ExternalReadResult == PackageManager.PERMISSION_GRANTED &&
                CameraResult == PackageManager.PERMISSION_GRANTED &&
                locationResult== PackageManager.PERMISSION_GRANTED;
    }
    private void requestpermission() {
        ActivityCompat.requestPermissions(this, new String[]
                {
                        READ_EXTERNAL_STORAGE,
                        CAMERA,
                        ACCESS_FINE_LOCATION
                }, RequestPermissionCode);
    }
    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults){
        init();
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
