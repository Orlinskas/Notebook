package com.orlinskas.notebook;

import com.orlinskas.notebook.MVVM.model.Notification;

public class CustomMockObjects {
    public static Notification getEmptyNotification() {
        return new Notification(0,1, 1, "no notes");
    }
}
