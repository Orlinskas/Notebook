package com.orlinskas.notebook.notificationHelper;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.orlinskas.notebook.mvvm.model.Notification;

public class NotificationAlarmSetter {
    public void set(Context context, Notification notification) {
        AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, NotificationAlarmReceiver.class);
        intent.putExtra("ID", notification.getId());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notification.getId(), intent, 0);
        if (alarmMgr != null) {
            alarmMgr.set(AlarmManager.RTC_WAKEUP, notification.getStartDateMillis(), pendingIntent);
        }
    }
}
