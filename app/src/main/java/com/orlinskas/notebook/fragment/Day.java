package com.orlinskas.notebook.fragment;

import com.orlinskas.notebook.database.Notification;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import java.util.List;

@Parcel(Parcel.Serialization.BEAN)
public class Day {
    private String dayName;
    private String dayDate;
    private List<Notification> notifications;

    @ParcelConstructor
    public Day(String dayName, String dayDate) {
        this.dayName = dayName;
        this.dayDate = dayDate;
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

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }
}
