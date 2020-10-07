package com.ynov.vernet.botbubulle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private int tHeures, tMinutes, tSecondes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Référencer les interface
        final TextView heures = (TextView) findViewById(R.id.heures);
        final TextView minutes = (TextView) findViewById(R.id.minutes);
        final TextView secondes = (TextView) findViewById(R.id.secondes);

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
                                Date date = Calendar.getInstance().getTime();
                                tHeures = 21 - date.getHours();
                                tMinutes = 59 - date.getMinutes();
                                tSecondes = 59 - date.getSeconds();

                                // Afficher le temps
                                heures.setText("" + tHeures + " h ");
                                minutes.setText("" + tMinutes + " mn ");
                                secondes.setText("" + tSecondes + " s");
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        thread.start();

    }
}