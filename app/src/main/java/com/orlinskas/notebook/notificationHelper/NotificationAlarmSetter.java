package com.orlinskas.notebook.notificationHelper;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.orlinskas.notebook.entity.Notification;

import org.parceler.Parcels;

import static com.orlinskas.notebook.Constants.PARCEL_NOTIFICATION;

public class NotificationAlarmSetter {
    public void set(Context context, Notification notification) {
        AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, NotificationAlarmReceiver.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(PARCEL_NOTIFICATION, Parcels.wrap(notification));
        intent.putExtras(bundle);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notification.getId(), intent, 0);
        if (alarmMgr != null) {
            alarmMgr.set(AlarmManager.RTC_WAKEUP, notification.getStartDateMillis(), pendingIntent);
        }
    }
}
