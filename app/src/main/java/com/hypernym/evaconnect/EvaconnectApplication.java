package com.hypernym.evaconnect;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.hypernym.evaconnect.utils.AppUtils;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class EvaconnectApplication extends Application {

    CalligraphyConfig mCalligraphyConfig;
    static EvaconnectApplication myAppInstance;

    public EvaconnectApplication() {
        myAppInstance = this;
    }

    public static EvaconnectApplication getInstance() {
        return myAppInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        CalligraphyConfig.initDefault(mCalligraphyConfig);

        AppUtils.setApplicationContext(getApplicationContext());

    }


}
