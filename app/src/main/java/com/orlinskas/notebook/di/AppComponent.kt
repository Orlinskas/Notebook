package com.orlinskas.notebook.di

import com.orlinskas.notebook.mvvm.fragment.ConnectionStatusFragment
import com.orlinskas.notebook.mvvm.viewModel.NotificationViewModel
import com.orlinskas.notebook.notificationHelper.NotificationStarter
import com.orlinskas.notebook.repository.NotificationRepository

import dagger.Component

@NotificationScope
@Component(modules = [RepositoryModule::class, ContextModule::class, LiveDataModule::class])
interface AppComponent {
    fun inject(notificationRepository: NotificationRepository)
    fun inject(fragment: ConnectionStatusFragment)
    fun inject(model: NotificationViewModel)
    fun inject(target: NotificationStarter)
}


