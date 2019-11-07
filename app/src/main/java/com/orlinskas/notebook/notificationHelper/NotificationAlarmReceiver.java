package com.orlinskas.notebook.notificationHelper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationAlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int id = intent.getIntExtra("ID", 1);
        context.startService(new Intent(context, NotificationShowService.class).putExtra("ID", id));
    }
}
