package com.ynov.vernet.botbubulle;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.biometrics.BiometricPrompt;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.Executors;

public class CodeActivity extends AppCompatActivity {

    EditText editTextCode;
    Button btnValider;
    TextView textViewBiometrie;
    Vibrator vibe;


    BiometricPrompt biometricPrompt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code);

        // Référencement des éléments graphiques
        editTextCode = findViewById(R.id.editTextCode);
        btnValider = findViewById(R.id.btnValider);
        textViewBiometrie = findViewById(R.id.textViewBiometrie);


        final CodeActivity biometrie = this;
        final Context context = this;

        // Ouvrir le clavier à l'ouverture de l'activité
        editTextCode.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        // Au clic du bouton valider
        btnValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Si le code est bon
                if (editTextCode.getText().toString().equals("2711")) {
                    // Démarrer l'activité principale
                    Toast.makeText(CodeActivity.this, "Code bon", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }

                // Si le code est mauvais
                else {
                    // Vider la zone de texte et afficher un message d'erreur
                    editTextCode.setText("");
                    editTextCode.setError("Mot de passe incorrect");
                    editTextCode.requestFocus();

                    // Masquer le message d'erreur au bout de 2s
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            editTextCode.setError(null);
                        }
                    }, 2000);

                    // Vibrer
                    vibe = (Vibrator) CodeActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
                    vibe.vibrate(400);
                }
            }
        });

        // Utiliser la reconnaissance biométrique
        textViewBiometrie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FingerprintManager fingerprintManager = (FingerprintManager) context.getSystemService(Context.FINGERPRINT_SERVICE);

                // Si l'appareil possède un capteur d'empreinte digitale
                assert fingerprintManager != null;
                if (fingerprintManager.isHardwareDetected() || fingerprintManager.hasEnrolledFingerprints()) {

                    // Demander l'authentification
                    biometricPrompt = new BiometricPrompt.Builder(getApplicationContext())
                            .setTitle("Biométrie")
                            .setDescription("Utilisez la biométrie pour vous connecter.")
                            .setNegativeButton("Se connecter grâce à un mot de passe", Executors.newSingleThreadExecutor(), new DialogInterface.OnClickListener() {
                                // Bouton annuler
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(getApplicationContext(), CodeActivity.class);
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
                            Intent intent = new Intent(getApplicationContext(), CodeActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            }
        });
    }

    // Bouton retour
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        // Lancer la classe Chargement
        Intent intent = new Intent(getApplicationContext(), LoadingActivity.class);
        startActivity(intent);
        finish();
    }
}