package com.ynov.vernet.botbubulle;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Code extends AppCompatActivity {

    EditText editTextCode;
    Button btnValider;
    Vibrator vibe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code);

        // Référencement des éléments graphiques
        editTextCode = findViewById(R.id.editTextCode);
        btnValider = findViewById(R.id.btnValider);

        // Ouvrir le clavier à l'ouverture de l'activité
        editTextCode.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        // Au clic du bouton
        btnValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Si le code est bon
                if (editTextCode.getText().toString().equals("2711")) {
                    // Démarrer l'activité principale
                    Toast.makeText(Code.this, "Code bon", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }

                // Si le code est mauvais
                else {
                    // Vider la zone de texte
                    editTextCode.setText("");
                    editTextCode.setError("Mot de passe incorrect");
                    editTextCode.requestFocus();

                    // Vibrer
                    vibe = (Vibrator) Code.this.getSystemService(Context.VIBRATOR_SERVICE);
                    vibe.vibrate(400);
                }
            }
        });
    }

    // Bouton retour
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        // Lancer la classe Chargement
        Intent intent = new Intent(getApplicationContext(), Chargement.class);
        startActivity(intent);
        finish();
    }
}