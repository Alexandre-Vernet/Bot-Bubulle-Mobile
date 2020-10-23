package com.ynov.vernet.botbubulle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.widget.Toast;

import java.util.concurrent.Executors;

public class Biometrie extends AppCompatActivity {

    BiometricPrompt biometricPrompt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biometrie);

        final Biometrie biometrie = this;

        // Demander l'authentification
        biometricPrompt = new BiometricPrompt.Builder(getApplicationContext())
                .setTitle("Biométrie")
                .setDescription("Utilisez la biométrie pour vous connecter.")
                .setNegativeButton("Se connecter grâce à un mot de passe", Executors.newSingleThreadExecutor(), new DialogInterface.OnClickListener() {
                    // Bouton annuler
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(), Code.class);
                        startActivity(intent);
                        finish();
                    }
                }).build();
        biometricPrompt.authenticate(new CancellationSignal(), Executors.newSingleThreadExecutor(), new BiometricPrompt.AuthenticationCallback() {
            // Si la connexion a reussi
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

                // Se connecter par code
                Intent intent = new Intent(getApplicationContext(), Code.class);
                startActivity(intent);
                finish();
            }
        });
    }
}