package com.orlinskas.notebook.notificationHelper;

import com.orlinskas.notebook.App;
import com.orlinskas.notebook.database.MyDatabase;
import com.orlinskas.notebook.date.DateCurrent;
import com.orlinskas.notebook.date.DateFormater;
import com.orlinskas.notebook.entity.Notification;

public class NotificationDeleter {
    public boolean delete(int deletedNotificationID) {
        MyDatabase database = App.getInstance().getMyDatabase();

        for(Notification notification : database.notificationDao().findAll()){
            if(notification.getId() == deletedNotificationID){
                database.notificationDao().delete(notification);
                return true;
            }
        }

        return false;
    }

    public boolean softDelete(int deletedNotificationID) {
        MyDatabase database = App.getInstance().getMyDatabase();

        for(Notification notification : database.notificationDao().findAll()){
            if(notification.getId() == deletedNotificationID){
                notification.setDeleted_at(DateCurrent.getLine(DateFormater.YYYY_MM_DD_HH_MM));
                notification.setSynchronized(false);
                database.notificationDao().update(notification);
                return true;
            }
        }

        return false;
    }
}
