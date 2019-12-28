package com.orlinskas.notebook.di;

import androidx.lifecycle.MutableLiveData;

import com.orlinskas.notebook.Enums;
import com.orlinskas.notebook.mvvm.model.Day;
import com.orlinskas.notebook.mvvm.model.Notification;

import java.util.List;

import dagger.Module;
import dagger.Provides;

@NotificationScope
@Module
public class LiveDataModule {
    @Provides
    public MutableLiveData<Enum<Enums.DownloadStatus>> repositoryStatusData() {
        return new MutableLiveData<>();
    }

    @Provides
    public MutableLiveData<Enum<Enums.ConnectionStatus>> connectionStatusData() {
        return new MutableLiveData<>();
    }

    @Provides
    public MutableLiveData<List<Notification>> allNotificationsData() {
        return new MutableLiveData<>();
    }

    @Provides
    public MutableLiveData<List<Day>> daysData() {
        return new MutableLiveData<>();
    }
}
