package com.orlinskas.notebook.builder;

import android.content.Context;

import com.orlinskas.notebook.notificationHelper.NotificationAlarmSetter;
import com.orlinskas.notebook.mvvm.model.Notification;
import com.orlinskas.notebook.date.DateFormater;

import java.util.Date;
import java.util.Random;

public class NotificationBuilder {
    private Context context;

    public NotificationBuilder(Context context) {
        this.context = context;
    }

    public Notification build(String bodyText, String startDateFull) {
        Date date = DateFormater.format(startDateFull, DateFormater.YYYY_MM_DD_HH_MM);
        int id = new Random().nextInt((2000000000 - 10) + 1) + 10;
        long createDateMillis = System.currentTimeMillis();
        long startDateMillis = date.getTime();

        Notification notification = new Notification(id, createDateMillis, startDateMillis, bodyText);

        NotificationAlarmSetter alarmSetter = new NotificationAlarmSetter();
        alarmSetter.set(context, notification);

        return notification;
    }
}