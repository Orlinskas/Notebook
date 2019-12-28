package com.orlinskas.notebook.di;

import androidx.lifecycle.MutableLiveData;

import com.orlinskas.notebook.Enums;
import com.orlinskas.notebook.mvvm.model.Day;
import com.orlinskas.notebook.mvvm.model.Notification;
import com.orlinskas.notebook.repository.NotificationRepository;

import java.util.List;

import dagger.Component;

@NotificationScope
@Component(modules = {NotificationModule.class, ContextModule.class, LiveDataModule.class})
public interface NotificationComponent {
    NotificationRepository getNotificationRepository();
    MutableLiveData<Enum<Enums.DownloadStatus>> getDownloadStatusData();
    MutableLiveData<Enum<Enums.ConnectionStatus>> getConnectionStatusData();
    MutableLiveData<List<Notification>> getAllNotificationsData();
    MutableLiveData<List<Day>> getDaysData();
}


