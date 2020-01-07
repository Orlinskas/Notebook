package com.orlinskas.notebook;

import com.orlinskas.notebook.mvvm.model.Notification;

public class MockObject {
    public static Notification getEmptyNotification() {
        return new Notification(0,1, 1, "no notes");
    }
}
