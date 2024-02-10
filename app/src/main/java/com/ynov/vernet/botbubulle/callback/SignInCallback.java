package com.ynov.vernet.botbubulle.callback;

public interface SignInCallback {
    void onSignInSuccess();
    void onSignInFailure(String errorMessage);
}
