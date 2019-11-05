package com.orlinskas.notebook.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@androidx.room.Database(entities = {Notification.class}, version = 1)
public abstract class Database extends RoomDatabase {
    public abstract NotificationDao notifiationDao();
}
