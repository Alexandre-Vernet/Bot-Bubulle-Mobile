package com.ynov.vernet.botbubulle;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;

import androidx.core.app.NotificationCompat;

public class Notification_Reciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        //Créer la notif
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Préparer la redirection au clic de la notif
        Intent repeating_intent = new Intent(context, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 100, repeating_intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Bouton "me rappeler demain"
        final PendingIntent rappelDemain =
                PendingIntent.getActivity(
                        context,
                        0,
                        new Intent(context, Chargement.class),
                        PendingIntent.FLAG_CANCEL_CURRENT
                );

        // Afficher la notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "Mon canal")
                .setContentText("Dring Dring ⏲ !")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.icon)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.icon))
                .addAction(R.drawable.ic_launcher_foreground, "Rappel dans 15h", rappelDemain)
                .addAction(R.drawable.ic_launcher_foreground, "Rappel dans 24h", rappelDemain)
                .setColor(context.getResources().getColor(R.color.colorPrimary))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);


        // Envoyer la notification
        notificationManager.notify(100, builder.build());
    }
}
