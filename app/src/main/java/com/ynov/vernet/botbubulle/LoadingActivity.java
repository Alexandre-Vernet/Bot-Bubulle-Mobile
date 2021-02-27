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

        // Play bubbles sound
        final MediaPlayer mediaPlayerBubbles = MediaPlayer.create(this, R.raw.bulles);
        mediaPlayerBubbles.start();

        // 2s of loading
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FingerprintManager fingerprintManager = (FingerprintManager) context.getSystemService(Context.FINGERPRINT_SERVICE);

                // If phone has fingerprint sensor
                assert fingerprintManager != null;
                if (fingerprintManager.isHardwareDetected() || fingerprintManager.hasEnrolledFingerprints()) {

                    // Log with fingerprint
                    Intent intent = new Intent(getApplicationContext(), BiometricsActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Log with code
                    Intent intent = new Intent(getApplicationContext(), CodeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 2000);
    }
}