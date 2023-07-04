package com.ynov.vernet.botbubulle;

import static com.ynov.vernet.botbubulle.SMS.generateRandomMessage;

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
    private static final String CANAL = "Daily notification";

    @Override
    public void onReceive(Context context, Intent intent) {
        this.intent = intent;
        sendNotification(context);
    }

    public static void sendNotification(Context context) {
        // Prepare onclick notification redirection
        Intent intent = new Intent(context, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 100, intent, PendingIntent.FLAG_IMMUTABLE);

        // Button "send sms"
        Intent iSMS = new Intent(context, SMS.class);
        PendingIntent pIntentSMS = PendingIntent.getBroadcast(context, 1, iSMS, PendingIntent.FLAG_IMMUTABLE);

        // Display notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CANAL)
                .setContentText(generateRandomMessage())
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.icon_notification)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.icon))
                .addAction(R.drawable.ic_launcher_foreground, "Send SMS", pIntentSMS)
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

        // Display notification
        notificationManager.notify(100, builder.build());
    }
}
