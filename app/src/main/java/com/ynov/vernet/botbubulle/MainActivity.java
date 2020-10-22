package com.ynov.vernet.botbubulle;

import android.os.Bundle;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {

    private int tHeures, tMinutes, tSecondes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Référencer les interface
        final TextView heures = findViewById(R.id.heures);
        final TextView minutes = findViewById(R.id.minutes);
        final TextView secondes = findViewById(R.id.secondes);

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