package com.example.balintbalina.myapplication;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
public class MessageReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        createNotification(context, "Drink Baby!", "Letelt a kért idő! Ellenőrzéshez kattints!");
    }

    public void createNotification(Context context, String msg, String msgText) {
        PendingIntent notificIntent = PendingIntent.getActivity(context, 0, new Intent(context, MessageActivity.class), 0);

        Notification.Builder mBuilder = new Notification.Builder(context)
                .setSmallIcon(R.drawable.titleimage)
                .setContentTitle(msg)
                .setContentText(msgText);

        mBuilder.setContentIntent(notificIntent);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());
    }

}
