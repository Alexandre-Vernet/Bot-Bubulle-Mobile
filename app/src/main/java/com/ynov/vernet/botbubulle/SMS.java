package com.ynov.vernet.botbubulle;

import static android.content.Context.VIBRATOR_SERVICE;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.widget.Toast;

import java.util.ArrayList;
import android.app.NotificationManager;

public class SMS extends BroadcastReceiver {

    MediaPlayer mediaPlayerMessageSent;

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            // Dismiss last notification
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(1);

            // Send SMS
            SmsManager sms = SmsManager.getDefault();
            ArrayList<String> parts = sms.divideMessage("Dring Dring 🕰️💊 !");
            String phoneNumber = context.getString(R.string.phoneNumber);
            if (phoneNumber.length() != 12) {
                Toast.makeText(context, R.string.error_format_phone_number, Toast.LENGTH_LONG).show();
                return;
            }
            sms.sendMultipartTextMessage(phoneNumber, null, parts, null, null);

            // Play sound
            mediaPlayerMessageSent = MediaPlayer.create(context, R.raw.ring);
            mediaPlayerMessageSent.start();

            // Vibrate
            Vibrator vibe = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
            vibe.vibrate(VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE));

            // Display toast
            Toast.makeText(context, R.string.message_sent, Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Toast.makeText(context, R.string.error_while_sending_message, Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}
