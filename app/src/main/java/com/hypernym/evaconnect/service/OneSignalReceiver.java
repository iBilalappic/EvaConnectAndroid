package com.hypernym.evaconnect.service;

import android.os.Bundle;
import android.util.Log;

import com.hypernym.evaconnect.models.Contents;
import com.hypernym.evaconnect.models.NotifyEvent;
import com.hypernym.evaconnect.models.PayloadNotification;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.Constants;
import com.hypernym.evaconnect.utils.GsonUtils;
import com.hypernym.evaconnect.utils.PrefUtils;
import com.hypernym.evaconnect.view.ui.activities.HomeActivity;
import com.hypernym.evaconnect.view.ui.fragments.EventDetailFragment;
import com.hypernym.evaconnect.view.ui.fragments.HomeFragment;
import com.hypernym.evaconnect.view.ui.fragments.PostDetailsFragment;
import com.hypernym.evaconnect.view.ui.fragments.SpecficJobFragment;
import com.onesignal.NotificationExtenderService;
import com.onesignal.OSNotificationReceivedResult;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;


public class OneSignalReceiver extends NotificationExtenderService {
    PayloadNotification payloadNotification;

    private EventBus bus = EventBus.getDefault();

    @Override
    protected boolean onNotificationProcessing(OSNotificationReceivedResult receivedResult) {
        // Read properties from result.
         JSONObject additionalData = receivedResult.payload.additionalData;
        Log.e("additionaldata", "" + additionalData.toString());
        if (receivedResult.isAppInFocus) {
            // listener.onNewNotification();

            if (additionalData != null) {
                try {
                    if(additionalData.getString("data") !=null)
                    {
                       int count= PrefUtils.getMessageCount(getApplicationContext());
                       count++;
                       PrefUtils.saveMessageCount(getApplicationContext(),count);
                        NotifyEvent event = new NotifyEvent("messageNotifcation");
                        bus.post(event);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    NotifyEvent event = new NotifyEvent("notifcation");
                    bus.post(event);
                }

            }

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
                Random random = new Random();
                int m = random.nextInt(9999 - 1000) + 1000;
                if (additionalData != null)
                {
                    Log.e("additionaldata", "bundle: " + bundle.toString());

                    if(additionalData.has("object_id"))
                    {
                        try {
                            Log.e("additionaldata", "object_id: " + additionalData.getString("object_id"));
                            Log.e("additionaldata", "object_type: " + additionalData.getString("object_type"));
                            if(additionalData.getString("object_id") !=null && additionalData.getString("object_type") !=null)
                            {
                                if(additionalData.getString("object_type").equalsIgnoreCase("post"))
                                {
                                    bundle.putInt("post",Integer.parseInt(additionalData.getString("object_id")));
                                    AppUtils.makeNotification(getApplication(), HomeActivity.class, PostDetailsFragment.class.getName(), bundle, payloadNotification.contents.en, true, m);
                                }
                                else if(additionalData.getString("object_type").equalsIgnoreCase("job")){
                                    bundle.putInt("job_id",Integer.parseInt(additionalData.getString("object_id")));
                                    AppUtils.makeNotification(getApplication(), HomeActivity.class, SpecficJobFragment.class.getName(), bundle, payloadNotification.contents.en, true, m);
                                }
                                else if(additionalData.getString("object_type").equalsIgnoreCase("event")){
                                    bundle.putInt("id",Integer.parseInt(additionalData.getString("object_id")));
                                    AppUtils.makeNotification(getApplication(), HomeActivity.class, EventDetailFragment.class.getName(), bundle, payloadNotification.contents.en, true, m);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                    else
                    {
                        AppUtils.makeNotification(getApplication(), HomeActivity.class, HomeFragment.class.getName(), bundle, payloadNotification.contents.en, true, m);
                    }
                }



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