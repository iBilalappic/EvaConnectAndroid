package com.hypernym.evaconnect.service;

import android.os.Bundle;
import android.util.Log;

import com.hypernym.evaconnect.models.Contents;
import com.hypernym.evaconnect.models.NotifyEvent;
import com.hypernym.evaconnect.models.PayloadNotification;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.Constants;
import com.hypernym.evaconnect.utils.GsonUtils;
import com.hypernym.evaconnect.view.ui.activities.HomeActivity;
import com.hypernym.evaconnect.view.ui.fragments.HomeFragment;
import com.onesignal.NotificationExtenderService;
import com.onesignal.OSNotificationReceivedResult;

import org.greenrobot.eventbus.EventBus;


public class OneSignalReceiver extends NotificationExtenderService {
    PayloadNotification payloadNotification;
    NotifyEvent event = new NotifyEvent("notifcation");
    private EventBus bus = EventBus.getDefault();

    @Override
    protected boolean onNotificationProcessing(OSNotificationReceivedResult receivedResult) {
        // Read properties from result.
        //  JSONObject additionalData = receivedResult.payload.body;
        // Log.e("test", additionalData.toString());
//        if (additionalData != null) {

        if (receivedResult.isAppInFocus) {
            // listener.onNewNotification();
            bus.post(event);
//            payloadNotification = new PayloadNotification();
//            try {
//                payloadNotification.contents = new Contents();
//                //  payloadNotification. = additionalData.getString("title");
//                payloadNotification.contents.en = receivedResult.payload.body;
//                // payloadNotification.notification_type = additionalData.getInt("notification_type");
//                Bundle bundle = new Bundle();
//                bundle.putString(Constants.PAYLOAD, GsonUtils.toJson(payloadNotification));
//                AppUtils.makeNotification(getApplication(), BaseActivity.class, HomeFragment.class.getName(), bundle, payloadNotification.contents.en, false, 0);
//
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

        } else {
            payloadNotification = new PayloadNotification();
            try {

                payloadNotification.contents = new Contents();
                // payloadNotification.title = additionalData.getString("title");
                payloadNotification.contents.en = receivedResult.payload.body;
                //  payloadNotification.notification_type = additionalData.getInt("notification_type");
                Bundle bundle = new Bundle();
                bundle.putString(Constants.PAYLOAD, GsonUtils.toJson(payloadNotification));
                AppUtils.makeNotification(getApplication(), HomeActivity.class, HomeFragment.class.getName(), bundle, payloadNotification.contents.en, false, 0);

            } catch (Exception e) {
                Log.e("TAAG", "" + e);
                e.printStackTrace();
            }
        }
        //  }
        // Return true to stop the notification from displaying.
        return true;
    }

}