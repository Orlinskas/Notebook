package com.orlinskas.notebook.builder;

import com.orlinskas.notebook.CustomMockObjects;
import com.orlinskas.notebook.date.DateCalculator;
import com.orlinskas.notebook.date.DateCurrent;
import com.orlinskas.notebook.date.DateFormater;
import com.orlinskas.notebook.mvvm.model.Notification;
import com.orlinskas.notebook.mvvm.model.Day;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.orlinskas.notebook.Constants.COUNT_DAYS_IN_LIST;

public class DaysBuilder {
    private String currentDate;
    private List<Notification> notifications;

    public DaysBuilder(List<Notification> notifications) {
        this.currentDate = DateCurrent.getLine(DateFormater.YYYY_MM_DD);
        this.notifications = notifications;
    }

    public List<Day> findActual() {
        List<Day> days = buildDaysList();

        for(Day day : days) {
            day.setNotifications(buildConcreteDayNotificationsList(day));
            days.set(days.indexOf(day), day);
        }

        return days;
    }

    private List<Day> buildDaysList() {
        List<Day> days = new ArrayList<>(COUNT_DAYS_IN_LIST);
        DateCalculator dateCalculator = new DateCalculator();

        for(int i = 0; i < COUNT_DAYS_IN_LIST; i++) {
            String dayDate = dateCalculator.plusDays(currentDate, i, DateFormater.YYYY_MM_DD);
            String dayName = dateCalculator.plusDays(currentDate, i, DateFormater.EEEE);
            days.add(new Day(dayName, dayDate));
        }

        return days;
    }

    private List<Notification> buildConcreteDayNotificationsList(Day day) {
        List<Notification> dayNotifications = new ArrayList<>();

        for(Notification notification : notifications) {
            Date date = new Date(notification.getStartDateMillis());
            String notificationDate = DateFormater.format(date, DateFormater.YYYY_MM_DD);

            if(day.getDayDate().equals(notificationDate)) {
                dayNotifications.add(notification);
            }
        }

        if(dayNotifications.size() == 0) {
            dayNotifications.add(CustomMockObjects.getEmptyNotification());
        }

        return dayNotifications;
    }
}
