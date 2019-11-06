package com.orlinskas.notebook.builder;

import com.orlinskas.notebook.date.DateParcer;
import com.orlinskas.notebook.entity.Notification;
import com.orlinskas.notebook.date.DateFormat;

import java.util.Date;

public class NotificationBuilder {
    public Notification build(String bodyText, String startDateFull) {
        Date date = DateParcer.parse(startDateFull, DateFormat.YYYY_MM_DD_HH_MM);
        long createDateMillis = System.currentTimeMillis();
        long startDateMillis = date.getTime();
        String startDayDate = DateParcer.parse(date, DateFormat.YYYY_MM_DD);
        String startDayTime = DateParcer.parse(date, DateFormat.HH_MM);
        String startDayName = DateParcer.parse(date, DateFormat.EEEE);

        return new Notification(createDateMillis, startDateMillis, startDateFull, startDayDate,
                startDayTime, startDayName, bodyText);
    }
}
