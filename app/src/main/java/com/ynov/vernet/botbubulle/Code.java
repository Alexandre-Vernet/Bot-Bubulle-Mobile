package com.ynov.vernet.botbubulle;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Code extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code);

        // Référencement
        final EditText editTextCode = findViewById(R.id.editTextCode);
        Button btnValider = findViewById(R.id.btnValider);

        // Au clic du bouton
        btnValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Vérifier si le code est bon
                if (editTextCode.getText().toString().equals("2711")) {
                    // Démarrer l'activité principale
                    Toast.makeText(Code.this, "Code bon", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                    // Si le code est mauvais
                } else {
                    editTextCode.setText("");
                    Toast.makeText(Code.this, "Code incorrect", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}