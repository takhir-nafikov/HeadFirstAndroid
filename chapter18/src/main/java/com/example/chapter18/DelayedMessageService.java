package com.example.chapter18;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.app.PendingIntent;
import android.app.NotificationManager;


public class DelayedMessageService extends IntentService {

    public static final String EXTRA_MESSAGE = "message";
    public static final int NOTIFICATION_ID = 5453;

    public DelayedMessageService() {
        super("DelayedMessageService");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onHandleIntent(Intent intent) {
        synchronized (this) {
            try {
                wait(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        String text = intent.getStringExtra(EXTRA_MESSAGE);
        showText(text);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showText(final String text) {
//        NotificationCompat.Builder builder =
//                new NotificationCompat.Builder(this)
//                        .setSmallIcon(android.R.drawable.sym_def_app_icon)
//                        .setContentTitle(getString(R.string.question))
//                        .setContentText(text)
//                        .setPriority(NotificationCompat.PRIORITY_HIGH)
//                        .setVibrate(new long[] {0, 1000})
//                        .setAutoCancel(true);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        String channelId = "some_channel_id";
        CharSequence channelName = "Some Channel";
        int importance = NotificationManager.IMPORTANCE_LOW;

        NotificationChannel notificationChannel =
                new NotificationChannel(channelId, channelName, importance);
        notificationChannel.setVibrationPattern(new long[]{0, 1000});

        notificationManager.createNotificationChannel(notificationChannel);

        NotificationCompat.Builder builder2 =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(android.R.drawable.sym_def_app_icon)
                        .setContentTitle(getString(R.string.question))
                        .setContentText(text)
                        .setAutoCancel(true);

        Intent actionIntent = new Intent(this, MainActivity.class);
        PendingIntent actionPendingIntent = PendingIntent.getActivity(
                this,
                0,
                actionIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        builder2.setContentIntent(actionPendingIntent);

        notificationManager.notify(NOTIFICATION_ID, builder2.build());
    }
}