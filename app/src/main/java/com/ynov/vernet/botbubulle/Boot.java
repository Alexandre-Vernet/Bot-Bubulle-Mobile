package com.ynov.vernet.botbubulle;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class Boot extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        // At phone boot
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

            // Implement Calendar
            final Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());

            // Send notification at 21h30
            calendar.set(Calendar.HOUR_OF_DAY, 21);
            calendar.set(Calendar.MINUTE, 30);
            calendar.set(Calendar.SECOND, 0);

            // Wake up phone to send notification
            AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            intent = new Intent(context, Notification.class);
            PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
            alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, alarmIntent);
        }
    }
}
