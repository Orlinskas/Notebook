package com.orlinskas.notebook.notificationHelper;

import com.orlinskas.notebook.R;
import com.orlinskas.notebook.entity.Notification;
import com.orlinskas.notebook.ui.view.MainView;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import java.util.Objects;

import static android.content.Context.NOTIFICATION_SERVICE;

class NotificationHomeScreenShower {
    void create(Context context, Notification userNotification) {
        Intent resultIntent = new Intent(context, MainView.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
