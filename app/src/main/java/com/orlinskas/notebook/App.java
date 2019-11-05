package com.orlinskas.notebook;

import android.app.Application;

import androidx.room.Room;

import com.orlinskas.notebook.database.MyDatabase;

public class App extends Application {

    public static App instance;

    private MyDatabase myDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        myDatabase = Room.databaseBuilder(this, MyDatabase.class, "notification")
                .build();
    }

    public static App getInstance() {
        return instance;
    }

    public MyDatabase getMyDatabase() {
        return myDatabase;
    }
}
