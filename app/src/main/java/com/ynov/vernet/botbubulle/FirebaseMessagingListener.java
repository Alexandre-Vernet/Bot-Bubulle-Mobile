package com.ynov.vernet.botbubulle;

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
        Authentication authentication = new Authentication(getApplicationContext());
        authentication.storeNotificationToken();
    }
}
