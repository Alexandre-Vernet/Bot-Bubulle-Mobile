package com.ynov.vernet.botbubulle;

import static android.content.Context.VIBRATOR_SERVICE;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.widget.Toast;

public class SMS extends BroadcastReceiver {

    String phoneNumber = "0782101533";
    String message = "Dring Dring ⏲ !";
    MediaPlayer sendingMessage;

    @Override
    public void onReceive(Context context, Intent intent) {

        // Add sound
        sendingMessage = MediaPlayer.create(context, R.raw.ring);

        try {

            // Dismiss last notification
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(100);

            // Send SMS
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);

            // Play sound
            sendingMessage.start();

            // Vibrate
            Vibrator vibe = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
            vibe.vibrate(VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE));


            Toast.makeText(context, R.string.message_sent, Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Toast.makeText(context, R.string.error_while_sending_message, Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}
