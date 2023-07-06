package com.ynov.vernet.botbubulle;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.Calendar;

public class Boot extends BroadcastReceiver {

    private static final String TAG = "Boot";

    @Override
    public void onReceive(Context context, Intent intent) {

        // At phone boot
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {

            // Implement Calendar
            final Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());

            // Get time set in memory
            SharedPreferences sp = context.getSharedPreferences("time", Activity.MODE_PRIVATE);
            int hours = sp.getInt("hours", 20);
            int minutes = sp.getInt("minutes", 0);

            // Set Calendar with time from memory
            calendar.set(Calendar.HOUR_OF_DAY, hours);
            calendar.set(Calendar.MINUTE, minutes);
            calendar.set(Calendar.SECOND, 0);

            // Create alarm to send notification at time from memory
            AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            intent = new Intent(context, Notification.class);
            PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);
            alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, alarmIntent);
        }
    }
}
