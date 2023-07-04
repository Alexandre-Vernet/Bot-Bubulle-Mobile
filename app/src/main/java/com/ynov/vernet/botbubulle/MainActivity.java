package com.ynov.vernet.botbubulle;

import static com.ynov.vernet.botbubulle.SMS.generateRandomMessage;

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
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TimePicker;

import androidx.annotation.RequiresApi;
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

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ask permission to send sms and notification
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.POST_NOTIFICATIONS}, 0);

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
        timePickerEditNotification.setOnTimeChangedListener((view, hourOfDay, minute) -> {

            Log.d(TAG, "onTimeChanged: ");
            // Get time from time picker
            int hours1 = timePickerEditNotification.getHour();
            int minutes1 = timePickerEditNotification.getMinute();

            // Save time in memory
            SharedPreferences sp1 = getSharedPreferences("time", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp1.edit();
            editor.putInt("hours", hours1);
            editor.putInt("minutes", minutes1);
            editor.apply();
        });

        // Send notification at time picker
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND, 0);

        // Wake up phone to send notification
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, Notification.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, alarmIntent);

//        sendNotification();
    }

    private void sendNotification() {
        // Prepare onclick notification redirection
        Intent repeating_intent = new Intent(context, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 100, repeating_intent, PendingIntent.FLAG_IMMUTABLE);

        // Button "send sms"
        Intent iSMS = new Intent(context, SMS.class);
        PendingIntent pIntentSMS = PendingIntent.getBroadcast(context, 1, iSMS, PendingIntent.FLAG_IMMUTABLE);

        // Display notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CANAL)
                .setContentText(generateRandomMessage())
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.icon_notification)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.icon))
                .addAction(R.drawable.ic_launcher_foreground, "Envoyer un msg", pIntentSMS)
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