package com.orlinskas.notebook.notificationHelper;

import com.orlinskas.notebook.App;
import com.orlinskas.notebook.database.MyDatabase;
import com.orlinskas.notebook.date.DateCurrent;
import com.orlinskas.notebook.date.DateFormater;
import com.orlinskas.notebook.entity.Notification;

public class NotificationDeleter {
    public boolean delete(int deletedNotificationID) {
//        NotificationRepository repository = new NotificationRepository();

//        CompletableFuture<List<Notification>> futureList = repository.findAll();
//        List<Notification> notifications;

//        try {
//            notifications = futureList.get();

//            for (Notification notification : notifications) {
//                if(notification.getId() == deletedNotificationID){
//                    repository.delete(notification);
//                    return true;
//                }

//            }
//        } catch (ExecutionException | InterruptedException e) {
//            e.printStackTrace();
//        }

        return false;
    }
}
