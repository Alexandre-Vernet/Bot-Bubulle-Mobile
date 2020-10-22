package com.ynov.vernet.botbubulle;

import android.content.Context;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class Chargement extends AppCompatActivity {

    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chargement);

        // Jouer le son des bulles
        final MediaPlayer sonBulles = MediaPlayer.create(this, R.raw.bulles);
        sonBulles.start();

        // Au bout de 2s de chargement
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FingerprintManager fingerprintManager = (FingerprintManager) context.getSystemService(Context.FINGERPRINT_SERVICE);

                // Vérifier si l'appareil possède un capteur d'empreinte digitale
                if (fingerprintManager.isHardwareDetected() || fingerprintManager.hasEnrolledFingerprints()) {
                    // Demander à se connecter par empreinte
                    Intent intent = new Intent(getApplicationContext(), Biometrie.class);
                    startActivity(intent);
                    finish();
                }

                // Sinon
                else {
                    // Demander à se connecter par code
                    Intent intent = new Intent(getApplicationContext(), Code.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 2000);
    }
}