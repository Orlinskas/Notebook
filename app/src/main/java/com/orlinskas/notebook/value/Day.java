package com.orlinskas.notebook.value;

import com.orlinskas.notebook.entity.Notification;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import java.util.List;
import java.util.Objects;

@Parcel(Parcel.Serialization.BEAN)
public class Day {
    private String dayDate;
    private String dayName;
    private List<Notification> notifications;

    @ParcelConstructor
    public Day(String dayName, String dayDate) {
        this.dayName = dayName;
        this.dayDate = dayDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Day day = (Day) o;
        return Objects.equals(dayName, day.dayName) &&
                Objects.equals(dayDate, day.dayDate) &&
                Objects.equals(notifications, day.notifications);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dayName, dayDate, notifications);
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
