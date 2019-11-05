package com.orlinskas.notebook;

import com.orlinskas.notebook.database.Notification;

public class CustomMockObjects {
    public static Notification getEmptyNotification() {
        return new Notification(404, 1, 1, "", "",
                "", "", "no notes");
    }
}
