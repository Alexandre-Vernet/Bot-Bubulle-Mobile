package com.ynov.vernet.botbubulle;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.widget.Toast;

public class SMS extends BroadcastReceiver {

    String noTel = "0641694541";
    //    String noTel = "0778555958";
    String message = "Dring Dring ⏲ !";
    MediaPlayer sendingMessage;
    Activity activity;
    private static final String TAG = "SMS";

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
            smsManager.sendTextMessage(noTel, null, message, null, null);

            // Play sound
            sendingMessage.start();

            // Vibrate
            Vibrator vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            long[] pattern = {0, 100};
            vibe.vibrate(pattern, -1);

            Toast.makeText(context, R.string.message_sent, Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Toast.makeText(context, R.string.error_while_sending_message, Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}
