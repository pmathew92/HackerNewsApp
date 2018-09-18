package com.prince.hackernewsapp.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.prince.hackernewsapp.R;

public class FirebaseMessageService extends FirebaseMessagingService {

    private static final String TAG = "FirebaseMessageService";

    private static final String CHANNEL_ID = "com.prince.hackernewsapp.CHANNEL_ONE";
    private static final String CHANNEL_NAME = "HackerNews Channel";
    private static final int RC_INT = 111;

    private NotificationManager notificationManager;

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.v(TAG, "New Token " + s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        createNotificationChannel();

        if (remoteMessage.getData().size() > 0) {
            Log.v(TAG, "Message data : " + remoteMessage.getData());
        }

        if (remoteMessage.getNotification() != null) {
            String title=remoteMessage.getNotification().getTitle();
            String body=remoteMessage.getNotification().getBody();
            buildNotification(title,body);
        }
    }


    /**
     * Method to display notifications having no particular click actions in it
     *
     * @param title
     * @param body
     */
    private void buildNotification(String title,String body) {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                .setContentText(body)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setAutoCancel(false);

        getManager().notify(RC_INT, notificationBuilder.build());
    }


    /**
     * Method to create a notification channel for Android O and above
     */
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            getManager().createNotificationChannel(channel);
        }
    }

    /**
     * Method to return a notification manager
     *
     * @return
     */
    private NotificationManager getManager() {
        if (notificationManager == null) {
            notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }
        return notificationManager;
    }
}
