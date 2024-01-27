package com.ynov.vernet.botbubulle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessagingListener extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Notification notification = new Notification();
        notification.sendNotification(getApplicationContext());
    }

    @Override
    public void onNewToken(String token) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//        if (firebaseAuth.getCurrentUser() != null) {
//            Authentication authentication = new Authentication(getApplicationContext());
//            authentication.storeNotificationToken();
//        }
    }
}
