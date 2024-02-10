package com.ynov.vernet.botbubulle;

import static com.ynov.vernet.botbubulle.firebase.Authentication.getUser;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.TimePicker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.ynov.vernet.botbubulle.callback.CronCallback;
import com.ynov.vernet.botbubulle.firebase.Firestore;

public class MainActivity extends AppCompatActivity {

    Context context;
    TimePicker timePickerEditNotification;

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check if user is logged
        if (getUser() == null) {
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
        timePickerEditNotification.setIs24HourView(true);

        // Set TimePicker to current cron
        getCurrentCron();

        // On time change, update cron in database
        timePickerEditNotification.setOnTimeChangedListener((view, hourOfDay, minute) -> {
            // Get time from time picker
            int hours = timePickerEditNotification.getHour();
            int minutes = timePickerEditNotification.getMinute();

            // Update cron in database
            updateCron(hours, minutes);
        });
    }


    private void getCurrentCron() {
        Firestore firestore = new Firestore(context);
        firestore.getCurrentCron(new CronCallback() {
            @Override
            public void onCronReceived(int[] cron) {
                int hours = cron[0];
                int minutes = cron[1];
                timePickerEditNotification.setHour(hours);
                timePickerEditNotification.setMinute(minutes);
            }

            @Override
            public void onFailure(String errorMessage) {
                timePickerEditNotification.setHour(12);
                timePickerEditNotification.setMinute(0);
            }
        });
    }

    private void updateCron(int hours, int minutes) {
        Firestore firestore = new Firestore(context);
        firestore.updateCron(hours, minutes);
    }
}
