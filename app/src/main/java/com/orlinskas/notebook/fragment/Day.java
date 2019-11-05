package com.orlinskas.notebook.fragment;

import com.orlinskas.notebook.database.Notification;

import org.parceler.Parcel;

import java.util.List;

@Parcel(Parcel.Serialization.BEAN)
class Day {
    private String dayName;
    private String dayDate;
    private List<Notification> notifications;

    public Day(String dayName, String dayDate, List<Notification> notifications) {
        this.dayName = dayName;
        this.dayDate = dayDate;
        this.notifications = notifications;
    }

    public String getDayName() {
        return dayName;
    }

    public String getDayDate() {
        return dayDate;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }
}
