package com.orlinskas.notebook.di

import androidx.lifecycle.MutableLiveData

import com.orlinskas.notebook.Enums
import com.orlinskas.notebook.mvvm.model.Day
import com.orlinskas.notebook.mvvm.model.Notification

import javax.inject.Singleton

import dagger.Module
import dagger.Provides

@Module
class LiveDataModule {
    @Provides
    fun repositoryStatusData(): MutableLiveData<Enum<Enums.DownloadStatus>> {
        return MutableLiveData()
    }

    @Provides
    fun connectionStatusData(): MutableLiveData<Enum<Enums.ConnectionStatus>> {
        return MutableLiveData()
    }

    @Provides
    fun allNotificationsData(): MutableLiveData<List<Notification>> {
        return MutableLiveData()
    }

    @Provides
    fun daysData(): MutableLiveData<List<Day>> {
        return MutableLiveData()
    }
}
