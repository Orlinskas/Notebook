package com.orlinskas.notebook.di.module

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
    @Singleton
    fun provideRepositoryStatusData(): MutableLiveData<Enum<Enums.DownloadStatus>> {
        return MutableLiveData()
    }

    @Provides
    @Singleton
    fun provideConnectionStatusData(): MutableLiveData<Enum<Enums.ConnectionStatus>> {
        return MutableLiveData()
    }

    @Provides
    @Singleton
    fun provideAllNotificationsData(): MutableLiveData<List<Notification>> {
        return MutableLiveData()
    }

    @Provides
    @Singleton
    fun provideDaysData(): MutableLiveData<List<Day>> {
        return MutableLiveData()
    }
}
