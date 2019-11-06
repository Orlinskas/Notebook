package com.orlinskas.notebook.builder;

import com.orlinskas.notebook.database.Notification;
import com.orlinskas.notebook.date.DateFormat;
import com.orlinskas.notebook.date.DateHelper;

import java.util.Date;

public class NotificationBuilder {
    public Notification build(String bodyText, String startDateFull) {
        Date date = DateHelper.parse(startDateFull, DateFormat.YYYY_MM_DD_HH_MM);
        long createDateMillis = System.currentTimeMillis();
        long startDateMillis = date.getTime();
        String startDayDate = DateHelper.parse(date, DateFormat.YYYY_MM_DD);
        String startDayTime = DateHelper.parse(date, DateFormat.HH_MM);
        String startDayName = DateHelper.parse(date, DateFormat.EEEE);

        return new Notification(createDateMillis, startDateMillis, startDateFull, startDayDate,
                startDayTime, startDayName, bodyText);
    }
}
