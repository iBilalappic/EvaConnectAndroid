package com.hypernym.evaconnect;

import android.app.Activity;
import android.app.Application;

import com.hypernym.evaconnect.utils.AppLogger;
import com.hypernym.evaconnect.utils.AppUtils;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class EvaconnectApplication extends Application {

    CalligraphyConfig mCalligraphyConfig;

    @Override
    public void onCreate() {
        super.onCreate();

        CalligraphyConfig.initDefault(mCalligraphyConfig);

        AppUtils.setApplicationContext(getApplicationContext());
    }
}
