package com.ynov.vernet.botbubulle;

import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.Executors;

public class Biometrie extends AppCompatActivity {

    final Biometrie biometrie = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biometrie);

        // Demander l'authentification
        BiometricPrompt biometricPrompt = new BiometricPrompt.Builder(getApplicationContext())
                .setTitle("Biométrie")
                .setDescription("Utilisez la biométrie pour vous connecter.")
                .setNegativeButton("Annuler", Executors.newSingleThreadExecutor(), new DialogInterface.OnClickListener() {
                    // Bouton annuler
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Passer au code numérique au lieu de l'empreinte
                        Intent intent = new Intent(getApplicationContext(), Code.class);
                        startActivity(intent);
                        finish();
                    }
                }).build();


        // Si la connexion a reussi
        biometricPrompt.authenticate(new CancellationSignal(), Executors.newSingleThreadExecutor(), new BiometricPrompt.AuthenticationCallback() {
            // Lorsqu'on est connecté
            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                biometrie.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Redirigé vers l'activité principale
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                        Toast.makeText(getApplicationContext(), "Authentifié !", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            // Si l'utilisateur clique ailleurs sur l'écran
            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);

                // Demander à se log par code
                Intent intent = new Intent(getApplicationContext(), Code.class);
                startActivity(intent);
                finish();
            }
        });
    }
}