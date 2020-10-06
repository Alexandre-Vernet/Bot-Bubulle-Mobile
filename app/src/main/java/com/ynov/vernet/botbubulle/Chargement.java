package com.ynov.vernet.botbubulle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;

public class Chargement extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chargement);

        // Jouer le son des bulles
        final MediaPlayer sonBulles = MediaPlayer.create(this, R.raw.bulles);
        sonBulles.start();

        // Démarrer l'activité Biométrie au bout de 2s
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), Biometrie.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }
}