package com.orlinskas.notebook.mvvm.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.orlinskas.notebook.App.Component.app
import com.orlinskas.notebook.Enums
import com.orlinskas.notebook.interactor.FindActualNotificationInteractor
import com.orlinskas.notebook.interactor.Interactor
import com.orlinskas.notebook.mvvm.model.Day
import com.orlinskas.notebook.repository.NotificationRepository
import javax.inject.Inject

class MainViewModel: ViewModel() {
    @Inject lateinit var repository: NotificationRepository
    @Inject lateinit var downloadStatusData: MutableLiveData<Enum<Enums.DownloadStatus>>
    @Inject lateinit var connectionStatusData: MutableLiveData<Enum<Enums.ConnectionStatus>>
    @Inject lateinit var daysData: MutableLiveData<List<Day>>

    private var interactor: Interactor = FindActualNotificationInteractor()

    init {
        app.getComponent().inject(this)
        interactor.execute()
    }
}
