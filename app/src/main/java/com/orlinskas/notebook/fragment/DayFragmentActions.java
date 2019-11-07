package com.orlinskas.notebook.fragment;

import com.orlinskas.notebook.value.Day;

public interface DayFragmentActions {
    void openDay(Day day);
    void deleteNotification(int deletedNotificationID);
}
