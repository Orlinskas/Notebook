package com.orlinskas.notebook.di.component

import androidx.lifecycle.MutableLiveData
import com.orlinskas.notebook.Enums
import com.orlinskas.notebook.di.module.*
import com.orlinskas.notebook.mvvm.fragment.ConnectionStatusFragment
import com.orlinskas.notebook.mvvm.model.Day
import com.orlinskas.notebook.mvvm.model.Notification
import com.orlinskas.notebook.mvvm.viewModel.MainViewModel
import com.orlinskas.notebook.mvvm.viewModel.NotificationViewModel
import com.orlinskas.notebook.notificationHelper.NotificationStarter
import com.orlinskas.notebook.repository.NotificationRepository

import dagger.Component
import dagger.Provides

@Component(modules = [RepositoryModule::class, LiveDataModule::class])
interface AppComponent {
    fun getRepo(): NotificationRepository
    fun getDownloadStatusData(): MutableLiveData<Enum<Enums.DownloadStatus>>
    fun getConnectionStatusData(): MutableLiveData<Enum<Enums.ConnectionStatus>>
    fun getAllNotificationsData(): MutableLiveData<List<Notification>>
    fun getDaysData(): MutableLiveData<List<Day>>
    fun inject(notificationRepository: NotificationRepository)
    fun inject(fragment: ConnectionStatusFragment)
    fun inject(model: MainViewModel)
    fun inject(model: NotificationViewModel)
    fun inject(target: NotificationStarter)
}


