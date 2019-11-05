package com.orlinskas.notebook.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Notification.class}, version = 1, exportSchema = false)
public abstract class MyDatabase extends RoomDatabase {
    public abstract NotificationDao notifiationDao();
}
