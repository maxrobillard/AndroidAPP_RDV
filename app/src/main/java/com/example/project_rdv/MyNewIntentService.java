package com.example.project_rdv;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;

import androidx.core.app.NotificationManagerCompat;

public class MyNewIntentService extends IntentService {
    private static final int NOTIFICATION_ID = (int) System.currentTimeMillis();
    public static final String NOTIFICATION_CHANNEL_ID = "10001";

    public MyNewIntentService() {
        super("MyNewIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Notification.Builder builder = new Notification.Builder(this);

        builder.setContentTitle(intent.getStringExtra("RDVTitle"));
        builder.setContentText(intent.getStringExtra("RDVTitle")+ " is coming in a few weeks");
        builder.setSmallIcon(R.drawable.ic_launcher_background);
        builder.setWhen(System.currentTimeMillis());

        Intent notifyIntent = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, (int)System.currentTimeMillis(), notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);


        Notification notificationCompat = builder.build();

        NotificationManager managerCompat =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);

            builder.setChannelId(NOTIFICATION_CHANNEL_ID);
            managerCompat.createNotificationChannel(notificationChannel);
        }

        //NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);

        managerCompat.notify(NOTIFICATION_ID, notificationCompat);
    }
}
