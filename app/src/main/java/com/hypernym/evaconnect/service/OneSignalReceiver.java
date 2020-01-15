package com.hypernym.evaconnect.service;

import android.os.Bundle;
import android.util.Log;

import com.hypernym.evaconnect.models.NotifyEvent;
import com.hypernym.evaconnect.models.PayloadNotification;
import com.onesignal.NotificationExtenderService;
import com.onesignal.OSNotificationReceivedResult;

import org.json.JSONException;
import org.json.JSONObject;


public class OneSignalReceiver extends NotificationExtenderService {
    PayloadNotification payloadNotification;
    NotifyEvent event = new NotifyEvent("notifcation");


    @Override
    protected boolean onNotificationProcessing(OSNotificationReceivedResult receivedResult) {


//

        // Read properties from result.
        JSONObject additionalData = receivedResult.payload.additionalData;
        Log.e("test", additionalData.toString());
        if (additionalData != null) {
            if (receivedResult.isAppInFocus) {
                payloadNotification = new PayloadNotification();
                try {
                    payloadNotification.title = additionalData.getString("title");
                    payloadNotification.message = additionalData.getString("message");
                    payloadNotification.notification_type = additionalData.getInt("notification_type");


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {

                try {
                    payloadNotification = new PayloadNotification();
                    payloadNotification.title = additionalData.getString("title");
                    payloadNotification.message = additionalData.getString("message");
                    payloadNotification.notification_type = additionalData.getInt("notification_type");

                } catch (Exception e) {
                    Log.e("TAAG", "" + e);
                    e.printStackTrace();
                }
            }
        }
        // Return true to stop the notification from displaying.
        return true;
    }
}