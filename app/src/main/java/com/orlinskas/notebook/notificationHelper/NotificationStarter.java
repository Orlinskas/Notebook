package com.orlinskas.notebook.notificationHelper;

import android.content.Context;
import android.os.AsyncTask;

import com.orlinskas.notebook.App;
import com.orlinskas.notebook.CustomMockObjects;
import com.orlinskas.notebook.database.MyDatabase;
import com.orlinskas.notebook.mvvm.model.Notification;

class NotificationStarter {
    void start(final Context context, final int id) {
        AsyncTask.execute(() -> {
            Notification notification = findNotification(id);
            NotificationHomeScreenShower notificationHomeScreenShower = new NotificationHomeScreenShower();
            if (notification.getCreateDateMillis() != 0) {
                notificationHomeScreenShower.create(context, notification);
            }
        });
    }

    private Notification findNotification(int id) {
        MyDatabase database = App.getInstance().getMyDatabase();
        for(Notification notification : database.notificationDao().findAll()) {
            if(notification.getId() == id) {
                return notification;
            }
        }

        return CustomMockObjects.getEmptyNotification();
    }
}
