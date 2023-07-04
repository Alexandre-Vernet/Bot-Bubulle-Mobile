package com.ynov.vernet.botbubulle;

import static com.ynov.vernet.botbubulle.Notification.sendNotification;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.TimePicker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    Context context;
    TimePicker timePickerEditNotification;

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ask permission to send sms and notification
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.POST_NOTIFICATIONS}, 0);

        context = getApplicationContext();
        timePickerEditNotification = findViewById(R.id.timePickerEditNotification);

        // Set TimePicker with values from memory
        SharedPreferences sp = getSharedPreferences("time", Activity.MODE_PRIVATE);
        int sharedPreferencesHours = sp.getInt("hours", 21);
        int sharedPreferencesMinutes = sp.getInt("minutes", 30);
        timePickerEditNotification.setHour(sharedPreferencesHours);
        timePickerEditNotification.setMinute(sharedPreferencesMinutes);

        // Implement Calendar to get current time and set alarm to send notification
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        // Wake up phone to send notification
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, Notification.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, alarmIntent);


        // On time change, save time in memory
        timePickerEditNotification.setOnTimeChangedListener((view, hourOfDay, minute) -> {
            // Get time from time picker
            int hours = timePickerEditNotification.getHour();
            int minutes = timePickerEditNotification.getMinute();

            // Save time in memory
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt("hours", hours);
            editor.putInt("minutes", minutes);
            editor.apply();
        });


        // Info : This is for testing purpose only
//        sendNotification(context);
    }
}