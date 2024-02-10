package com.ynov.vernet.botbubulle.firebase;

import static com.ynov.vernet.botbubulle.firebase.Authentication.getUser;
import android.content.Context;
import android.util.Log;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.ynov.vernet.botbubulle.R;
import com.ynov.vernet.botbubulle.callback.CronCallback;

import java.util.HashMap;
import java.util.Map;

public class Firestore {

    private final Context context;
    private static FirebaseFirestore firebaseFirestore;

    public Firestore(Context context) {
        this.context = context;
    }

    public static FirebaseFirestore getFirebaseFirestore() {
        if (firebaseFirestore == null) {
            firebaseFirestore = FirebaseFirestore.getInstance();
        }
        return firebaseFirestore;
    }

    public void getCurrentCron(CronCallback callback) {
        getFirebaseFirestore().collection("notifications").document(getUser().getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String cron = documentSnapshot.getString("cron");
                        if (cron != null) {
                            String[] cronArray = cron.split(" ");
                            int hours = Integer.parseInt(cronArray[1]);
                            int minutes = Integer.parseInt(cronArray[0]);
                            callback.onCronReceived(new int[]{hours, minutes});
                        } else {
                            callback.onFailure("Cron value is null");
                        }
                    } else {
                        callback.onFailure("Document does not exist");
                    }
                })
                .addOnFailureListener(e -> {
                    callback.onFailure(e.getMessage());
                    Log.d("TAG", "getCurrentCron: " + e.getMessage());
                });
    }


    public void updateCron(int hours, int minutes) {
        if (hours < 0 || hours > 23 || minutes < 0 || minutes > 59) {
            throw new IllegalArgumentException(context.getString(R.string.hours_and_minutes_must_be_between_0_and_23_for_hours_and_0_and_59_for_minutes));
        }

        // Convert hours and minutes to cron
        String cronExpression = String.format("%d %d * * *", minutes, hours);

        Map<String, Object> notification = new HashMap<>();
        notification.put("cron", cronExpression);

        getFirebaseFirestore().collection("notifications").document(getUser().getUid())
                .set(notification, SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                })
                .addOnFailureListener(e -> {
                    Log.d("TAG", "updateCron: " + e.getMessage());
                });
    }
}
