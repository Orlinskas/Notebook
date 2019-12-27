package com.orlinskas.notebook;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.orlinskas.notebook.database.MyDatabase;
import com.orlinskas.notebook.entity.Notification;
import com.orlinskas.notebook.repository.NotificationRepository;
import com.orlinskas.notebook.service.ApiFactory;
import com.orlinskas.notebook.service.NotificationApiService;
import com.orlinskas.notebook.value.Day;

import java.util.List;

public class App extends Application {
    public static App instance;
    private MyDatabase myDatabase;
    private NotificationApiService remoteService;
    private MutableLiveData<List<Notification>> allNotificationsData;
    private MutableLiveData<List<Day>> daysData;
    private MutableLiveData<Enum<Enums.RepositoryStatus>> repositoryStatusData;
    private MutableLiveData<Enum<Enums.ConnectionStatus>> connectionStatusData;
    private NotificationRepository repository;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        myDatabase = Room.databaseBuilder(this, MyDatabase.class, "notification").build();
        remoteService = ApiFactory.INSTANCE.getNotificationApi();
        allNotificationsData = new MutableLiveData<>();
        daysData = new MutableLiveData<>();
        repositoryStatusData = new MutableLiveData<>();
        connectionStatusData = new MutableLiveData<>();
        repository = new NotificationRepository();
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

    public MutableLiveData<List<Day>> getDaysLiveData() {
        return daysData;
    }

    public MutableLiveData<Enum<Enums.RepositoryStatus>> getRepositoryStatusData() {
        return repositoryStatusData;
    }

    public MutableLiveData<Enum<Enums.ConnectionStatus>> getConnectionStatusData() {
        return connectionStatusData;
    }

    public NotificationRepository getRepository() {
        return repository;
    }

    public NotificationApiService getRemoteService() {
        return remoteService;
    }
}
