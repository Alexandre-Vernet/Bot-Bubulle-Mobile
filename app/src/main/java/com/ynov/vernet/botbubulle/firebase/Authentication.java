package com.ynov.vernet.botbubulle.firebase;

import static com.ynov.vernet.botbubulle.firebase.Messaging.getFirebaseMessaging;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.SetOptions;
import com.ynov.vernet.botbubulle.R;
import com.ynov.vernet.botbubulle.callback.SignInCallback;

import java.util.HashMap;
import java.util.Map;

public class Authentication {

    private final Context context;
    private static FirebaseAuth firebaseAuth;

    public Authentication(Context context) {
        this.context = context;
    }

    public static FirebaseAuth getFirebaseAuth() {
        if (firebaseAuth == null) {
            firebaseAuth = FirebaseAuth.getInstance();
        }
        return firebaseAuth;
    }

    public static FirebaseUser getUser() {
        if (firebaseAuth == null) {
            firebaseAuth = FirebaseAuth.getInstance();
        }
        return firebaseAuth.getCurrentUser();
    }

    public void storeNotificationToken() {
        getFirebaseMessaging().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(context, R.string.error_fetching_fcm_registration_token, Toast.LENGTH_LONG).show();
                        return;
                    }

                    // Get new FCM registration token
                    String token = task.getResult();

                    Map<String, Object> notification = new HashMap<>();
                    notification.put("token", token);

                    if (getUser() != null) {
                        Firestore.getFirebaseFirestore().collection("notifications").document(getUser().getUid())
                                .set(notification, SetOptions.merge())
                                .addOnSuccessListener(aVoid -> {
                                })
                                .addOnFailureListener(e -> {
                                });
                    }
                });
    }

    public void signIn(String email, String password, SignInCallback signInCallback) {
        getFirebaseAuth().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Store notification token
                        storeNotificationToken();

                        signInCallback.onSignInSuccess();
                    }
                })
                .addOnFailureListener(e -> signInCallback.onSignInFailure(e.getMessage()));
    }
}
