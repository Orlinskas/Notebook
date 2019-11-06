package com.orlinskas.notebook.builder;

import com.orlinskas.notebook.entity.Notification;
import com.orlinskas.notebook.date.DateFormater;

import java.util.Date;

public class NotificationBuilder {
    public Notification build(String bodyText, String startDateFull) {
        Date date = DateFormater.format(startDateFull, DateFormater.YYYY_MM_DD_HH_MM);
        long createDateMillis = System.currentTimeMillis();
        long startDateMillis = date.getTime();

        return new Notification(createDateMillis, startDateMillis, bodyText);
    }
}
