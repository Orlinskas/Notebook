package com.orlinskas.notebook.di.component

import com.orlinskas.notebook.di.module.*
import com.orlinskas.notebook.mvvm.fragment.ConnectionStatusFragment
import com.orlinskas.notebook.mvvm.fragment.DayViewModel
import com.orlinskas.notebook.mvvm.viewModel.BaseViewModel
import com.orlinskas.notebook.mvvm.viewModel.ConcreteDayViewModel
import com.orlinskas.notebook.mvvm.viewModel.CreateNotificationModel
import com.orlinskas.notebook.mvvm.viewModel.MainViewModel
import dagger.Component
import javax.inject.Singleton

@Component(modules = [RepositoryModule::class, LiveDataModule::class, SynchronizerModule::class,
                        NetworkHandlerModule::class, InteractorModule::class])

@Singleton
interface AppComponent {
    fun inject(viewModel: MainViewModel)
    fun inject(viewModel: BaseViewModel)
    fun inject(viewModel: ConcreteDayViewModel)
    fun inject(viewModel: CreateNotificationModel)
    fun inject(viewModel: DayViewModel)
    fun inject(fragment: ConnectionStatusFragment)
}


