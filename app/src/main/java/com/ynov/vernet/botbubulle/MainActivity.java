package com.ynov.vernet.botbubulle;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {

    Context context;
    private int tHeures, tMinutes, tSecondes;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();

        // Référencer les interface
        final TextView heures = findViewById(R.id.heures);
        final TextView minutes = findViewById(R.id.minutes);
        final TextView secondes = findViewById(R.id.secondes);
        progressBar = findViewById(R.id.progressBar);

        // Vérifier l'heure pour l'envoie de la notification
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 19);
        calendar.set(Calendar.MINUTE, 34);
        calendar.set(Calendar.SECOND, 0);

        // Préparer la classe qui envoie la notification
        Intent intent = new Intent(new Intent(getApplicationContext(), Notification_Reciever.class));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Créer l'alarme
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);


        // Mettre à jour le temps avant la prochaine notification
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        // Actualiser toutes les secondes le thread
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Masquer la barre de chargement
                                progressBar.setVisibility(View.INVISIBLE);

                                // Récupérer le temps avant 22h
                                Date date = new Date();
                                Calendar calendar = GregorianCalendar.getInstance();
                                calendar.setTime(date);
                                tHeures = 21 - calendar.get(Calendar.HOUR_OF_DAY);
                                tMinutes = 59 - calendar.get(Calendar.MINUTE);
                                tSecondes = 59 - calendar.get(Calendar.SECOND);

                                // Afficher le temps
                                heures.setText("" + tHeures + " h ");
                                minutes.setText("" + tMinutes + " mn ");
                                secondes.setText("" + tSecondes + " s");
                            }
                        });
                    }
                } catch (InterruptedException ignored) {
                }
            }
        };

        thread.start();
    }
}