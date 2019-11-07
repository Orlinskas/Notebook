package com.orlinskas.notebook.notificationHelper;

import android.content.Context;
import android.content.Intent;

import androidx.legacy.content.WakefulBroadcastReceiver;

import sm.euzee.github.com.servicemanager.ServiceManager;

public class NotificationAlarmReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        final int id = intent.getIntExtra("ID", 1);

        ServiceManager.runService(context, () -> {
            NotificationStarter starter = new NotificationStarter();
            starter.start(context, id);
        }, true);
    }
}
