package com.orlinskas.notebook.MVVM.fragment;

import com.orlinskas.notebook.MVVM.model.Notification;

public interface DayFragmentActions {
    void openDay(int dayID);
    void deleteNotification(Notification notification);
}
