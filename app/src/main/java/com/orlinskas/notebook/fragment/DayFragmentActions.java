package com.orlinskas.notebook.fragment;

import com.orlinskas.notebook.entity.Notification;
import com.orlinskas.notebook.value.Day;

public interface DayFragmentActions {
    void openDay(int dayID);
    void deleteNotification(Notification notification);
}
