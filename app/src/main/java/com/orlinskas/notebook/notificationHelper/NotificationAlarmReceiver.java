package com.orlinskas.notebook.notificationHelper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.orlinskas.notebook.entity.Notification;

import org.parceler.Parcels;

import java.util.Objects;

import static com.orlinskas.notebook.Constants.PARCEL_NOTIFICATION;

public class NotificationAlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Notification notification = Parcels.unwrap(Objects.requireNonNull(intent.getExtras()).getParcelable(PARCEL_NOTIFICATION));
        NotificationHomeScreenCreator notificationHomeScreenCreator = new NotificationHomeScreenCreator();
        if (notification != null) {
            notificationHomeScreenCreator.create(context, notification);
        }
    }
}
