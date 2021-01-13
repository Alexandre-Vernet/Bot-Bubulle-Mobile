package com.ynov.vernet.botbubulle;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class Recall extends BroadcastReceiver {

    int interval = 1 * 60000;       /*recall in 2 minutes*/
    boolean sendNotification = true;
    private static final String TAG = "Rappel30Mn";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (sendNotification) {
            // Dismiss notification
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(100);

            // Prepare notification
            intent = new Intent(new Intent(context, Notification.class));
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            // Create Alarm
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis(), interval, pendingIntent);
        }
        sendNotification = false;
    }
}
