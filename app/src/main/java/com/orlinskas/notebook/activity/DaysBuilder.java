package com.orlinskas.notebook.activity;

import com.orlinskas.notebook.App;
import com.orlinskas.notebook.database.MyDatabase;
import com.orlinskas.notebook.database.Notification;
import com.orlinskas.notebook.date.DateCalculator;
import com.orlinskas.notebook.date.DateFormat;
import com.orlinskas.notebook.date.DateHelper;
import com.orlinskas.notebook.fragment.Day;

import java.util.ArrayList;
import java.util.List;

public class DaysBuilder {
    private final int COUNT_DAYS_IN_LIST = 7;
    private String currentDate;

    public DaysBuilder() {
        this.currentDate = DateHelper.getCurrent(DateFormat.YYYY_MM_DD);
    }

    public List<Day> findActual() {
        MyDatabase database = App.getInstance().getMyDatabase();
        List<Notification> notifications = database.notifiationDao().findActual(System.currentTimeMillis());

        List<Day> days = buildDaysList();

        for(Day day : days) {
            days.set(days.indexOf(day), writeNotification(day, notifications));
        }

        return days;
    }

    private ArrayList<Day> buildDaysList() {
        ArrayList<Day> days = new ArrayList<>(COUNT_DAYS_IN_LIST);
        DateCalculator dateCalculator = new DateCalculator();

        for(int i = 0; i < COUNT_DAYS_IN_LIST; i++) {
            String dayDate = dateCalculator.plusDays(currentDate, i, DateFormat.YYYY_MM_DD);
            String dayName = dateCalculator.plusDays(currentDate, i, DateFormat.EEEE);
            days.add(new Day(dayName, dayDate));
        }

        return days;
    }

    private Day writeNotification(Day day, List<Notification> notifications) {
        for(Notification notification : notifications) {
            if(day.getDayDate().equals(notification.getStart_day_date())) {
                day.getNotifications().add(notification);
            }
        }

        return day;
    }
}
