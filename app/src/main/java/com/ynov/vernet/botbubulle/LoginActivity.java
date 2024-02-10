package com.ynov.vernet.botbubulle;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.ynov.vernet.botbubulle.callback.SignInCallback;
import com.ynov.vernet.botbubulle.databinding.ActivityLoginBinding;
import com.ynov.vernet.botbubulle.firebase.Authentication;

public class LoginActivity extends AppCompatActivity {


    private EditText emailEditText;
    private ProgressBar loadingProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityLoginBinding binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        emailEditText = binding.email;
        final EditText passwordEditText = binding.password;
        final Button loginButton = binding.login;
        loadingProgressBar = binding.loading;

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginButton.setEnabled(isFormLoginValid(emailEditText.getText().toString(),
                        passwordEditText.getText().toString()));
            }
        };
        emailEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);

        loginButton.setOnClickListener(v -> signIn(emailEditText.getText().toString(),
                passwordEditText.getText().toString()));
    }

    public void signIn(String email, String password) {
        showProgressBar(true);

        Authentication authentication = new Authentication(this);
        authentication.signIn(email, password, new SignInCallback() {
            @Override
            public void onSignInSuccess() {
                // Start activity
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onSignInFailure(String errorMessage) {
                emailEditText.setError(errorMessage);

                // Hide progress bar
                showProgressBar(false);
            }
        });
    }

    private boolean isFormLoginValid(String email, String password) {
        if (!email.contains("@")) {
            return false;
        }
        return password.length() >= 7;
    }

    private void showProgressBar(boolean show) {
        if (show) {
            loadingProgressBar.setVisibility(View.VISIBLE);
        } else {
            loadingProgressBar.setVisibility(View.GONE);
        }
    }
}
