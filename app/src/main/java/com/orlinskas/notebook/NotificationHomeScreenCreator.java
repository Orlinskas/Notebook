package com.orlinskas.notebook;

import com.orlinskas.notebook.activity.MainActivity;
import com.orlinskas.notebook.entity.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import java.util.Objects;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationHomeScreenCreator {
    public void create(Context context, Notification userNotification) {
        Intent resultIntent = new Intent(context, MainActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Your reminder")
                        .setContentText(userNotification.getBodyText())
                        .setContentIntent(resultPendingIntent)
                        .setAutoCancel(true)
                        .setWhen(userNotification.getStartDateMillis())
                        .setPriority(2);

        android.app.Notification notification = builder.build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        Objects.requireNonNull(notificationManager).notify(userNotification.getId(), notification);
    }
}
