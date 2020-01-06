package com.orlinskas.notebook.mvvm.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.orlinskas.notebook.App.Component.app
import com.orlinskas.notebook.Enums
import com.orlinskas.notebook.interactor.FindActualNotificationUseCase
import com.orlinskas.notebook.mvvm.model.Day
import javax.inject.Inject

class MainViewModel: ViewModel() {
    @Inject lateinit var downloadStatusData: MutableLiveData<Enum<Enums.DownloadStatus>>
    @Inject lateinit var connectionStatusData: MutableLiveData<Enum<Enums.ConnectionStatus>>
    @Inject lateinit var daysData: MutableLiveData<List<Day>>
    @Inject lateinit var findActualNotificationUseCase: FindActualNotificationUseCase

    init {
        app.getComponent().inject(this)
        downloadStatusData.postValue(Enums.DownloadStatus.LOADING)
    }

    fun findActual() {
        findActualNotificationUseCase {
            try {
                daysData.postValue(it.data as List<Day>?)
                connectionStatusData.postValue(when (it.code) {
                    200 -> Enums.ConnectionStatus.CONNECTION_DONE
                    404 -> Enums.ConnectionStatus.CONNECTION_FAIL
                    else -> Enums.ConnectionStatus.CONNECTION_FAIL
                })
            } catch (e: Exception) {
                //error
            } finally {
                downloadStatusData.postValue(Enums.DownloadStatus.READY)
            }
        }
    }
}
