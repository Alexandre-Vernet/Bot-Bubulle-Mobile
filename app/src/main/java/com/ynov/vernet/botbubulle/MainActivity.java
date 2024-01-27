package com.ynov.vernet.botbubulle;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.TimePicker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    Context context;
    TimePicker timePickerEditNotification;

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check if user is logged
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }

        // Ask permission to send sms and notification
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.SEND_SMS,
                Manifest.permission.POST_NOTIFICATIONS,
                Manifest.permission.RECEIVE_BOOT_COMPLETED,
                Manifest.permission.WAKE_LOCK
        }, 0);

        context = getApplicationContext();
        timePickerEditNotification = findViewById(R.id.timePickerEditNotification);

        // Set TimePicker with values from memory
        SharedPreferences sp = getSharedPreferences("time", Activity.MODE_PRIVATE);
        int sharedPreferencesHours = sp.getInt("hours", 20);
        int sharedPreferencesMinutes = sp.getInt("minutes", 0);
        timePickerEditNotification.setHour(sharedPreferencesHours);
        timePickerEditNotification.setMinute(sharedPreferencesMinutes);

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
    }
}
