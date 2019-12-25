package com.orlinskas.notebook;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.orlinskas.notebook.database.MyDatabase;
import com.orlinskas.notebook.entity.Notification;
import com.orlinskas.notebook.value.Day;

import java.util.List;

public class App extends Application {
    public static App instance;
    private MyDatabase myDatabase;
    private MutableLiveData<List<Notification>> actualNotificationsData;
    private MutableLiveData<List<Notification>> allNotificationsData;
    private MutableLiveData<List<Day>> daysData;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        myDatabase = Room.databaseBuilder(this, MyDatabase.class, "notification")
                .build();
        actualNotificationsData = new MutableLiveData<>();
        allNotificationsData = new MutableLiveData<>();
        daysData = new MutableLiveData<>();
    }

    public static App getInstance() {
        return instance;
    }

    public MyDatabase getMyDatabase() {
        return myDatabase;
    }

    public MutableLiveData<List<Notification>> getAllNotificationsLiveData() {
        return allNotificationsData;
    }

    public MutableLiveData<List<Notification>> getActualNotificationsLiveData() {
        return actualNotificationsData;
    }

    public MutableLiveData<List<Day>> getDaysLiveData() {
        return daysData;
    }
}
