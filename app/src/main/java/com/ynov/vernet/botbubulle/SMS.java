package com.ynov.vernet.botbubulle;

import static android.content.Context.VIBRATOR_SERVICE;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.widget.Toast;

import java.util.ArrayList;

public class SMS extends BroadcastReceiver {

    MediaPlayer mediaPlayerMessageSent;

    @Override
    public void onReceive(Context context, Intent intent) {
        // Add sound
        mediaPlayerMessageSent = MediaPlayer.create(context, R.raw.ring);

        try {
            // Dismiss last notification
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(1);

            // Send SMS
            SmsManager sms = SmsManager.getDefault();
            ArrayList<String> parts = sms.divideMessage(generateRandomMessage());
            String phoneNumber = context.getString(R.string.phoneNumber);
            if (phoneNumber.length() != 12) {
                Toast.makeText(context, R.string.error_format_phone_number, Toast.LENGTH_LONG).show();
                return;
            }
            sms.sendMultipartTextMessage(phoneNumber, null, parts, null, null);

            // Play sound
            mediaPlayerMessageSent.start();

            // Vibrate
            Vibrator vibe = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
            vibe.vibrate(VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE));

            // Display toast
            Toast.makeText(context, R.string.message_sent, Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Toast.makeText(context, R.string.error_while_sending_message, Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public static String generateRandomMessage() {
        String[] messages = {
                "Dring Dring 🕰️💊 !",
                "Hey toi, n'oublie pas ta petite pilule magique aujourd'hui ! Elle a un don pour empêcher les surprises non désirées. Allez, avale-la et reste tranquille ! 😉",
                "Attention, mission anti-bébé en cours : prends ta pilule contraceptive et garde la porte fermée aux cigognes ! 🚫👶",
                "Ne laisse pas la pilule te jouer un tour : prends-la maintenant et assure-toi que les bébés ne viennent pas te faire coucou à l'improviste !",
                "La pilule contraceptive te rappelle : 'Je suis la petite pilule qui te dit : Pas de bébé aujourd'hui, merci !' Ne l'oublie pas !",
                "S'il te plaît, prends ta pilule contraceptive aujourd'hui, sinon ton calendrier risque de se remplir avec des anniversaires de bébés non planifiés. 😅",
                "Ne laisse pas ta pilule se sentir délaissée ! Prends-la maintenant et montre-lui que tu es un maître de la planification familiale 👍",
                "Attention, la pilule est ton alliée secrète contre les bébés surprises. Ne laisse pas ton superpouvoir de contrôle des naissances s'éteindre aujourd'hui ! 💊💪",
                "Chère pilule contraceptive, je sais que parfois tu as l'impression d'être oubliée, mais aujourd'hui, je te promets de ne pas te ghoster. Sois prête à être avalée ! 😀",
                "Psst... Toi là-bas ! N'oublie pas ta pilule aujourd'hui, sinon les cigognes pourraient organiser une manifestation surprise devant chez toi. 🦢",
                "Hey toi, prête pour le grand spectacle de la contraception ? Prends ta pilule et assure-toi que la salle de bébé reste fermée à clé ! 🎩🐰",
                "Rappel comique : ne laisse pas ta pilule contraceptive se sentir abandonnée, elle aimerait être prise avec autant d'attention que ton téléphone. 📱💊",
                "Ne fais pas attendre ta pilule comme si c'était le dernier épisode de ta série préférée. Montre-lui un peu d'amour et prends-la maintenant ! 📺😉",
                "La pilule contraceptive dit : 'Si tu m'oublies aujourd'hui, prépare-toi à chanter la berceuse des couches et des biberons !' Prends-la et sauve tes tympans ! 🎶🍼"
        };
        int random = (int) (Math.random() * messages.length);
        return messages[0];
    }
}
