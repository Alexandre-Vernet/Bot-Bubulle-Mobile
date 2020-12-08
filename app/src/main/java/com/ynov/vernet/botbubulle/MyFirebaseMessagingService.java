package com.ynov.vernet.botbubulle;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String CHANNEL_ID = "Mon canal";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Uri sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + this.getPackageName() + "/" + R.raw.bulles);  //Here is FILE_NAME is the name of file that you want to play
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build();


        // Créer la notification
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.icon)
                .setContentText("Dring Dring !")

                //Vibration
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setStyle(new NotificationCompat.BigTextStyle())

                //LED
                .setLights(Color.RED, 3000, 3000)

                //Ton
//        builder.setSound(Uri.parse("uri://sadfasdfasdf.mp3"));
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        // Envoyer la notification
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);

            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setSound(sound, attributes); // This is IMPORTANT
            channel.setDescription(description);
            NotificationManager notificationManager2 = getSystemService(NotificationManager.class);
            notificationManager2.createNotificationChannel(channel);
        }

        notificationManager.notify(1, notificationBuilder.build());
    }
}
