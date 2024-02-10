package com.ynov.vernet.botbubulle.firebase;

import static com.ynov.vernet.botbubulle.firebase.Authentication.getUser;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.ynov.vernet.botbubulle.Notification;

public class Messaging  extends FirebaseMessagingService {

    public static FirebaseMessaging firebaseMessaging;

    public Messaging() {
    }

    public static FirebaseMessaging getFirebaseMessaging() {
        if (firebaseMessaging == null) {
            firebaseMessaging = FirebaseMessaging.getInstance();
        }
        return FirebaseMessaging.getInstance();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Notification notification = new Notification();
        notification.sendNotification(getApplicationContext());
    }

    @Override
    public void onNewToken(String token) {
        if (getUser() != null) {
            Authentication authentication = new Authentication(getApplicationContext());
            authentication.storeNotificationToken();
        }
    }
}
