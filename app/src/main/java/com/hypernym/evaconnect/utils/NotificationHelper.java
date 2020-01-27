package com.hypernym.evaconnect.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;

import com.hypernym.evaconnect.R;

/**
 * Created by Metis on 04-Jun-18.
 */

public class NotificationHelper extends ContextWrapper {

    public static final String CHANNEL_ONE_ID = "com.logistics.hypernym.notifcation.ONE";
    public static final String CHANNEL_ONE_NAME = "Channel One";
    private NotificationManager notifManager;


    @RequiresApi(api = Build.VERSION_CODES.O)
    public NotificationHelper(Context base) {
        super(base);

        createchannel();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createchannel() {
        NotificationChannel androidChannel = new NotificationChannel(CHANNEL_ONE_ID,
                CHANNEL_ONE_NAME, NotificationManager.IMPORTANCE_DEFAULT);
        // Sets whether notifications posted to this channel should display notification lights
        androidChannel.enableLights(true);
        // Sets whether notification posted to this channel should vibrate.
        androidChannel.enableVibration(true);
        // Sets the notification light color for notifications posted to this channel
        androidChannel.setLightColor(Color.GREEN);
        // Sets whether notifications posted to this channel appear on the lockscreen or not
        androidChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getManager().createNotificationChannel(androidChannel);
    }


    NotificationManager getManager() {
        if (notifManager == null) {
            notifManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return notifManager;
    }

    private static int getNotificationIcon() {
        boolean useWhiteIcon = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.logo : R.mipmap.logo;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Notification.Builder getAndroidChannelNotification(Context context, Class<?> class_, String fragmentName,
                                                              Bundle bundle, String message, boolean isUpdateCurrent,
                                                              int requestCode) {
        Intent intent = new Intent(context, class_);
        if (isUpdateCurrent) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.putExtra(Constants.FRAGMENT_NAME, fragmentName);
        intent.putExtra(Constants.DATA, bundle);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT
        );
        return new Notification.Builder(getApplicationContext(), CHANNEL_ONE_ID)
                .setContentText(message)
                .setSmallIcon(getNotificationIcon())
                .setTicker(message)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentTitle(context.getString(R.string.app_name))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
    }


}
