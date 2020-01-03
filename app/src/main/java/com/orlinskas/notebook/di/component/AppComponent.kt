package com.orlinskas.notebook.di.component

import com.orlinskas.notebook.di.module.LiveDataModule
import com.orlinskas.notebook.di.module.RepositoryModule
import com.orlinskas.notebook.mvvm.fragment.ConnectionStatusFragment
import com.orlinskas.notebook.mvvm.fragment.DayViewModel
import com.orlinskas.notebook.mvvm.viewModel.BaseViewModel
import com.orlinskas.notebook.mvvm.viewModel.ConcreteDayViewModel
import com.orlinskas.notebook.mvvm.viewModel.CreateNotificationModel
import com.orlinskas.notebook.mvvm.viewModel.MainViewModel
import com.orlinskas.notebook.notificationHelper.NotificationStarter
import com.orlinskas.notebook.repository.NotificationRepository
import dagger.Component
import javax.inject.Singleton

@Component(modules = [RepositoryModule::class, LiveDataModule::class])
@Singleton
interface AppComponent {
    fun inject(notificationRepository: NotificationRepository)
    fun inject(fragment: ConnectionStatusFragment)
    fun inject(model: MainViewModel)
    fun inject(model: BaseViewModel)
    fun inject(model: ConcreteDayViewModel)
    fun inject(model: CreateNotificationModel)
    fun inject(model: DayViewModel)
    fun inject(target: NotificationStarter)
}


