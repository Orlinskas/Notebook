package com.orlinskas.notebook;

import com.orlinskas.notebook.database.MyDatabase;
import com.orlinskas.notebook.entity.Notification;

public class NotificationDeleter {
    public boolean delete(int deletedNotificationID) {
        MyDatabase database = App.getInstance().getMyDatabase();

        for(Notification notification : database.notifiationDao().findAll()){
            if(notification.getId() == deletedNotificationID){
                database.notifiationDao().delete(notification);
                return true;
            }
        }

        return false;
    }
}
