package com.ynov.vernet.botbubulle;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    Context context;
    TimePicker timePickerEditNotification;
    Button btnUpdateNotification;

    private static final String CANAL = "Notification quotidienne";
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();
        timePickerEditNotification = findViewById(R.id.timePickerEditNotification);
        btnUpdateNotification = findViewById(R.id.btnUpdateNotification);

        // Set TimePicker with time to send notification
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        int hour = sharedPref.getInt("hour", 21);
        int minutes = sharedPref.getInt("minutes", 30);
        timePickerEditNotification.setHour(hour);
        timePickerEditNotification.setMinute(minutes);

        // Implement Calendar
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        // Change time to send notification
        timePickerEditNotification.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                int hour = timePickerEditNotification.getHour();
                int minutes = timePickerEditNotification.getMinute();

                // Save time in memory
                SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("hour", hour);
                editor.putInt("minutes", minutes);
                editor.apply();
            }
        });

        // Send notification at specific time
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND, 0);

        // Wake up phone to send notification
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, Notification.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

//        alarmMgr.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, alarmIntent);
    }

    private void envoyerNotification() {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

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
                .addAction(R.drawable.ic_launcher_foreground, "Rappel dans 30mn", pIntentRappel30Mn)
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