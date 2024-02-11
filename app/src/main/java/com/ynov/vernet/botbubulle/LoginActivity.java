package com.ynov.vernet.botbubulle;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.ynov.vernet.botbubulle.callback.SignInCallback;
import com.ynov.vernet.botbubulle.databinding.ActivityLoginBinding;
import com.ynov.vernet.botbubulle.firebase.Authentication;

public class LoginActivity extends AppCompatActivity {


    private EditText emailEditText;
    private ProgressBar loadingProgressBar;


    private final int RC_SIGN_IN = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityLoginBinding binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        emailEditText = binding.email;
        final EditText passwordEditText = binding.password;
        final Button loginButton = binding.login;
        final SignInButton googleSignInButton = binding.googleSignIn;
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


        googleSignInButton.setOnClickListener(v -> signInWithGoogle());
    }

    private void signIn(String email, String password) {
        showProgressBar(true);

        Authentication authentication = new Authentication(this);
        authentication.signIn(email, password, new SignInCallback() {
            @Override
            public void onSignInSuccess() {
                startMainActivity();
            }

            @Override
            public void onSignInFailure(String errorMessage) {
                emailEditText.setError(errorMessage);

                // Hide progress bar
                showProgressBar(false);
            }
        });
    }

    private void signInWithGoogle() {
        showProgressBar(true);

        Intent intent = new Authentication(this).signInWithGoogle();
        startActivityForResult(intent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            showProgressBar(false);
            Task<GoogleSignInAccount> googleSignInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = googleSignInAccountTask.getResult();
                if (account != null) {
                    AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                    FirebaseAuth.getInstance().signInWithCredential(credential)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    new Authentication(this).storeNotificationToken();
                                    startMainActivity();
                                }
                            });
                }
            } catch (Exception e) {
                emailEditText.setError(e.getMessage());

                // Hide progress bar
                showProgressBar(false);
            }
        }
    }

    private void startMainActivity() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
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
