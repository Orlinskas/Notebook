package com.orlinskas.notebook.mvvm.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.orlinskas.notebook.App.Component.app
import com.orlinskas.notebook.Enums
import com.orlinskas.notebook.interactor.CreateNotificationUseCase
import com.orlinskas.notebook.interactor.DeleteNotificationUseCase
import com.orlinskas.notebook.interactor.FindActualNotificationUseCase
import com.orlinskas.notebook.mvvm.model.Day
import com.orlinskas.notebook.mvvm.model.Notification
import com.orlinskas.notebook.repository.NotificationRepository
import kotlinx.coroutines.*
import javax.inject.Inject

class BaseViewModel : ViewModel() {
    @Inject lateinit var repository: NotificationRepository
    @Inject lateinit var downloadStatusData: MutableLiveData<Enum<Enums.DownloadStatus>>
    @Inject lateinit var connectionStatusData: MutableLiveData<Enum<Enums.ConnectionStatus>>
    @Inject lateinit var daysData: MutableLiveData<List<Day>>
    @Inject lateinit var createNotificationUseCase: CreateNotificationUseCase
    @Inject lateinit var findActualNotificationUseCase: FindActualNotificationUseCase
    @Inject lateinit var deleteNotificationUseCase: DeleteNotificationUseCase

    private val job: Job = Job()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    init {
        app.getComponent().inject(this)
    }

    fun findActual() {
        downloadStatusData.postValue(Enums.DownloadStatus.LOADING)

        findActualNotificationUseCase(params = System.currentTimeMillis()) {
            try {
                daysData.postValue(it.data as List<Day>?)
                handleConnection(it.code)
            } catch (e: Exception) {
                //error
            } finally {
                downloadStatusData.postValue(Enums.DownloadStatus.READY)
            }
        }
    }

    fun createNotification(notification: Notification) {
        downloadStatusData.postValue(Enums.DownloadStatus.LOADING)

        createNotificationUseCase(notification) {
            handleConnection(it.code)
            updateDaysData()
            downloadStatusData.postValue(Enums.DownloadStatus.READY)
        }
    }

    fun deleteNotification(notification: Notification) {
        downloadStatusData.postValue(Enums.DownloadStatus.LOADING)

        deleteNotificationUseCase(notification) {
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