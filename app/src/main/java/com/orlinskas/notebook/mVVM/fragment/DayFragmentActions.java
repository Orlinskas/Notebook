package com.orlinskas.notebook.mVVM.fragment;

import com.orlinskas.notebook.mVVM.model.Notification;

public interface DayFragmentActions {
    void openDay(int dayID);
    void deleteNotification(Notification notification);
}
