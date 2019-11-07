package com.orlinskas.notebook.notificationHelper;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.orlinskas.notebook.App;
import com.orlinskas.notebook.CustomMockObjects;
import com.orlinskas.notebook.database.MyDatabase;
import com.orlinskas.notebook.entity.Notification;

public class NotificationShowService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final int id = intent.getIntExtra("ID", 0);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                Notification notification = findNotification(id);
                NotificationHomeScreenShower notificationHomeScreenShower = new NotificationHomeScreenShower();
                if (notification.getCreateDateMillis() != 0) {
                    notificationHomeScreenShower.create(getApplicationContext(), notification);
                }
            }
        });

        return super.onStartCommand(intent, flags, startId);
    }

    private Notification findNotification(int id) {
        MyDatabase database = App.getInstance().getMyDatabase();
        for(Notification notification : database.notifiationDao().findAll()) {
            if(notification.getId() == id) {
                return notification;
            }
        }

        return CustomMockObjects.getEmptyNotification();
    }
}
