package com.ynov.vernet.botbubulle;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private int tHeures, tMinutes, tSecondes;
    Context context;
    ProgressBar progressBar;
    private static final String CHANNEL_ID = "Mon canal";
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Référencer les interface
        final TextView heures = findViewById(R.id.heures);
        final TextView minutes = findViewById(R.id.minutes);
        final TextView secondes = findViewById(R.id.secondes);
        progressBar = findViewById(R.id.progressBar);


        // Vérifier l'heure pour l'envoie de la notif
        Calendar now = Calendar.getInstance();

        int hour = now.get(Calendar.HOUR_OF_DAY); // Get hour in 24 hour format
        int minute = now.get(Calendar.MINUTE);

        Date date = parseDate(hour + ":" + minute);
        Date dateCompareOne = parseDate("09:56");
        Date dateCompareTwo = parseDate("11:00");

        if (dateCompareOne.before(date) && dateCompareTwo.after(date)) {
            Log.d(TAG, "run: Dring Dring !");
            envoyerNotification();
        }


        // Démarrer un thread
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

    private Date parseDate(String date) {

        final String inputFormat = "HH:mm";
        SimpleDateFormat inputParser = new SimpleDateFormat(inputFormat, Locale.FRANCE);
        try {
            return inputParser.parse(date);
        } catch (java.text.ParseException e) {
            return new Date(0);
        }
    }

    private void envoyerNotification() {

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);

        final PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        MainActivity.this,
                        0,
                        intent,
                        PendingIntent.FLAG_CANCEL_CURRENT
                );
//
//
//        // Créer la notification
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentText("Dring Dring ⏲ !")
                .setSmallIcon(R.drawable.icon)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.icon))

                .setColor(getResources().getColor(R.color.colorPrimary))
//                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})

//                .setSound(Uri.parse("https://www.youtube.com/watch?v=W6YOhFCGZMM&list=WL&index=2"))


                .setSound(Uri.parse("android.resource://com.ynov.vernet.botbubulle/" + R.raw.bulles))

                .setDefaults(-1)


                .setStyle(new NotificationCompat.BigTextStyle().bigText("Big View Styles"))
                .setContentIntent(resultPendingIntent)
                .addAction(R.drawable.ic_launcher_foreground, "Add", resultPendingIntent)


                .setPriority(NotificationCompat.PRIORITY_HIGH);


        // Envoyer la notification
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(1, notificationBuilder.build());

    }
}