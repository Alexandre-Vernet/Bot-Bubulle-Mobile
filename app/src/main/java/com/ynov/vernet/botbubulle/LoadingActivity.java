package com.ynov.vernet.botbubulle;

import android.content.Context;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chargement);

        final Context context = this;

        // Jouer le son des bulles
        final MediaPlayer sonBulles = MediaPlayer.create(this, R.raw.bulles);
        sonBulles.start();

        // Au bout de 2s de chargement
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FingerprintManager fingerprintManager = (FingerprintManager) context.getSystemService(Context.FINGERPRINT_SERVICE);

                // Si l'appareil possède un capteur d'empreinte digitale
                assert fingerprintManager != null;
                if (fingerprintManager.isHardwareDetected() || fingerprintManager.hasEnrolledFingerprints()) {

                    // Se connecter par empreinte
                    Intent intent = new Intent(getApplicationContext(), BiometricsActivity.class);
                    startActivity(intent);
                    finish();
                }

                // Si l'appareil ne possède pas de capteur d'empreinte digitale
                else {
                    // Se connecter par code
                    Intent intent = new Intent(getApplicationContext(), CodeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 2000);
    }
}