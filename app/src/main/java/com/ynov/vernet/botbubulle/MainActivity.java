package com.ynov.vernet.botbubulle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.widget.Toast;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Executor executor = Executors.newSingleThreadExecutor();

        // Demander l'authentification
        BiometricPrompt biometricPrompt = new BiometricPrompt.Builder(this)
                .setTitle("Biométrie")
                .setDescription("Utilisez la biométrie pour vous connecter.")
                .setNegativeButton("Annuler", executor, new DialogInterface.OnClickListener() {
                    // Bouton annuler
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Arrêter l'application
                        finish();
                    }
                }).build();

        final MainActivity mainActivity = this;

        biometricPrompt.authenticate(new CancellationSignal(), executor, new BiometricPrompt.AuthenticationCallback() {
            // Lorsqu'on est connecté
            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Afficher un toast
                        Toast.makeText(MainActivity.this, "Authentifié !", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}