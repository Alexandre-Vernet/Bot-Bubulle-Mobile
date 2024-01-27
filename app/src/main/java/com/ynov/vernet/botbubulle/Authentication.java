package com.ynov.vernet.botbubulle;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

public class Authentication {

    private final Context context;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    public Authentication(Context context) {
        this.context = context;
    }

    public Authentication(Context context, FirebaseAuth firebaseAuth, FirebaseFirestore firebaseFirestore) {
        this.context = context;
        this.firebaseAuth = firebaseAuth;
        this.firebaseFirestore = firebaseFirestore;
    }

    void storeNotificationToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(context, R.string.error_fetching_fcm_registration_token, Toast.LENGTH_LONG).show();
                        return;
                    }

                    FirebaseUser user = firebaseAuth.getCurrentUser();

                    // Get new FCM registration token
                    String token = task.getResult();

                    Map<String, Object> notification = new HashMap<>();
                    notification.put("token", token);

                    if (user != null) {
                        firebaseFirestore.collection("notifications").document(user.getUid())
                                .set(notification, SetOptions.merge())
                                .addOnSuccessListener(aVoid -> {})
                                .addOnFailureListener(e -> {});
                    }
                });
    }
}
