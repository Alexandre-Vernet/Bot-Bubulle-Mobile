package com.ynov.vernet.botbubulle;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;

import androidx.core.app.NotificationCompat;

public class Notification extends BroadcastReceiver {

    Intent intent;

    private static final String TAG = "Notification";

    @Override
    public void onReceive(Context context, Intent intent) {
        this.intent = intent;

        // Create notification
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Prepare notification
        Intent repeating_intent = new Intent(context, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 100, repeating_intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Button remember in 30mn
        Intent iRappel30Mn = new Intent(context, Notification.class);
        PendingIntent pIntentRappel = PendingIntent.getBroadcast(context, 1, iRappel30Mn, PendingIntent.FLAG_UPDATE_CURRENT);


        // Display notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "Mon canal")
                .setContentText("Dring Dring ⏲ !")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.icon)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.icon))
//                .addAction(R.drawable.ic_launcher_foreground, "Rappel dans 30mn", pIntentRappel)
                .setColor(context.getResources().getColor(R.color.colorPrimary))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        notificationManager.notify(100, builder.build());
    }
}
