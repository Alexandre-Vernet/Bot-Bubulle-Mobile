package com.ynov.vernet.botbubulle;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

public class Notification extends BroadcastReceiver {

    Intent intent;
    private static final String CANAL = "Notification quotidienne";
    private static final String TAG = "Notification";

    @Override
    public void onReceive(Context context, Intent intent) {
        this.intent = intent;

        // At phone boot
//        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
//            // Send notification at 21:30 at reboot phone
//            Calendar calendar = Calendar.getInstance();
//            calendar.set(Calendar.HOUR_OF_DAY, 21);
//            calendar.set(Calendar.MINUTE, 30);
//            calendar.set(Calendar.SECOND, 0);
//
//            // Prepare notification
//            intent = new Intent(new Intent(context, Notification.class));
//            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//            // Create alarm
//            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
//        }


        // Prepare onclick notification redirection
        Intent repeating_intent = new Intent(context, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 100, repeating_intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Button "remember in 30mn"
        Intent iRappel30Mn = new Intent(context, Recall.class);
        PendingIntent pIntentRappel30Mn = PendingIntent.getBroadcast(context, 1, iRappel30Mn, PendingIntent.FLAG_UPDATE_CURRENT);

        // Display notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CANAL)
                .setContentText("Dring Dring ⏲ !")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.icon_notification)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.icon))
                //.addAction(R.drawable.ic_launcher_foreground, "Rappel dans 30mn", pIntentRappel30Mn)
                .setColor(ContextCompat.getColor(context, R.color.notification))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);


        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Create channel
        String channelId = "id";
        String channelDescription = "desc";
        NotificationChannel notificationChannel = new NotificationChannel(channelId, CANAL, NotificationManager.IMPORTANCE_DEFAULT);
        notificationChannel.setDescription(channelDescription);
        notificationManager.createNotificationChannel(notificationChannel);
        builder.setChannelId(channelId);

        // Send notification
        notificationManager.notify(100, builder.build());
    }
}
