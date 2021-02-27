package com.ynov.vernet.botbubulle;

import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.Executors;

public class BiometricsActivity extends AppCompatActivity {

    BiometricPrompt biometricPrompt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biometrie);

        final BiometricsActivity biometricsActivity = this;

        // Ask authentication
        biometricPrompt = new BiometricPrompt.Builder(getApplicationContext())
                .setTitle(getString(R.string.biometrics))
                .setDescription(getString(R.string.use_biometrics_to_log_in))
                .setNegativeButton(getString(R.string.login_password), Executors.newSingleThreadExecutor(), new DialogInterface.OnClickListener() {
                    // Bouton annuler
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(), CodeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }).build();
        biometricPrompt.authenticate(new CancellationSignal(), Executors.newSingleThreadExecutor(), new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                biometricsActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                        Toast.makeText(getApplicationContext(), getString(R.string.authenticated), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            // If user click elsewhere on screen
            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);

                // Log with code
                Intent intent = new Intent(getApplicationContext(), CodeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}