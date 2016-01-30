package com.attila.wakerupper;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

public class NotificationFactory {

    private static int _notificationId;

    public static void enableNotification(Context context, Class parentActivity, boolean textEnabled, boolean phoneEnabled) {

        StringBuilder _sbBuilder = new StringBuilder("Checking for ");
        if( textEnabled )
            _sbBuilder.append("texts");
        if( phoneEnabled )
            _sbBuilder.append(" and phone calls");
        _sbBuilder.append(".");

        NotificationCompat.Builder _builder = new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Monitoring")
                        .setContentText(_sbBuilder.toString())
                        .setOngoing(true);

        //stolen from google's example on how to bind a notification to an activity
        Intent resultIntent = new Intent(context, parentActivity);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(parentActivity);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        _builder.setContentIntent(resultPendingIntent);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(_notificationId, _builder.build());
    }

    public static void disableNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(_notificationId);
    }
}
