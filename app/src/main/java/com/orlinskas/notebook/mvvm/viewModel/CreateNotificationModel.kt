package com.orlinskas.notebook.mvvm.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.orlinskas.notebook.App.Component.app
import com.orlinskas.notebook.Enums
import com.orlinskas.notebook.interactor.CreateNotificationUseCase
import com.orlinskas.notebook.interactor.FindActualNotificationUseCase
import com.orlinskas.notebook.mvvm.model.Day
import com.orlinskas.notebook.mvvm.model.Notification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import javax.inject.Inject

class CreateNotificationModel : ViewModel() {
    @Inject lateinit var downloadStatusData: MutableLiveData<Enum<Enums.DownloadStatus>>
    @Inject lateinit var connectionStatusData: MutableLiveData<Enum<Enums.ConnectionStatus>>
    @Inject lateinit var daysData: MutableLiveData<List<Day>>
    @Inject lateinit var createNotificationUseCase: CreateNotificationUseCase
    @Inject lateinit var findActualNotificationUseCase: FindActualNotificationUseCase

    private var job: Job = Job()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    init {
        app.getComponent().inject(this)
    }

    fun createNotification(notification: Notification) {
        downloadStatusData.postValue(Enums.DownloadStatus.LOADING)

        createNotificationUseCase(notification) {
            handleConnection(it.code)
            updateDaysData()
            downloadStatusData.postValue(Enums.DownloadStatus.READY)
        }
    }

    private fun updateDaysData() {
        findActualNotificationUseCase(params = System.currentTimeMillis()) {
            try {
                daysData.postValue(it.data as List<Day>?)
                handleConnection(it.code)
            } catch (e: Exception) {
                //error
            }
        }
    }

    private fun handleConnection(code: Int) {
        connectionStatusData.postValue(when (code) {
            200 -> Enums.ConnectionStatus.CONNECTION_DONE
            404 -> Enums.ConnectionStatus.CONNECTION_FAIL
            else -> Enums.ConnectionStatus.CONNECTION_FAIL
        })
    }

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }
}
