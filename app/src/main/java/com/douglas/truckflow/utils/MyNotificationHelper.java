package com.douglas.truckflow.utils;// MyNotificationHelper.java
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import com.douglas.truckflow.R;

public class MyNotificationHelper {
    public static void createDefaultNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Default Channel";
            String description = "Default notification channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            String channelId = context.getString(R.string.default_notification_channel_id);

            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
