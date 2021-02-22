package com.ynov.vernet.botbubulle;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    Context context;
    TimePicker timePickerEditNotification;

    private static final String CANAL = "Notification quotidienne";
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ask permission send sms
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 0);

        context = getApplicationContext();
        timePickerEditNotification = findViewById(R.id.timePickerEditNotification);

        // Set TimePicker with time to send notification
        SharedPreferences sp = getSharedPreferences("time", Activity.MODE_PRIVATE);
        int hours = sp.getInt("hours", 21);
        int minutes = sp.getInt("minutes", 30);

        timePickerEditNotification.setHour(hours);
        timePickerEditNotification.setMinute(minutes);

        // Implement Calendar
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        // Change time to send notification
        timePickerEditNotification.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

                Log.d(TAG, "onTimeChanged: ");
                // Get time from time picker
                int hours = timePickerEditNotification.getHour();
                int minutes = timePickerEditNotification.getMinute();

                // Save time in memory
                SharedPreferences sp = getSharedPreferences("time", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("hours", hours);
                editor.putInt("minutes", minutes);
                editor.apply();
            }
        });

        // Send notification at time picker
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND, 0);

        // Wake up phone to send notification
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, Notification.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, alarmIntent);
    }

    private void sendNotification() {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Prepare onclick notification redirection
        Intent repeating_intent = new Intent(context, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 100, repeating_intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Button "remember in 30mn"
        Intent iSMS = new Intent(context, SMS.class);
        PendingIntent pIntentSMS = PendingIntent.getBroadcast(context, 1, iSMS, PendingIntent.FLAG_UPDATE_CURRENT);

        // Display notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CANAL)
                .setContentText("Dring Dring ⏲ !")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.icon_notification)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.icon))
                .addAction(R.drawable.ic_launcher_foreground, "Envoyer un msg", pIntentSMS)
                .setColor(ContextCompat.getColor(context, R.color.notification))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);


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