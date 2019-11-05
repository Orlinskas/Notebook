package com.orlinskas.notebook;

import android.app.Application;

import androidx.room.Room;

import com.orlinskas.notebook.database.Database;

public class App extends Application {

    public static App instance;

    private Database database;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(getApplicationContext(), Database.class, "notification")
                .build();
    }

    public static App getInstance() {
        return instance;
    }

    public Database getDatabase() {
        return database;
    }
}
