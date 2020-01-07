package com.orlinskas.notebook.mvvm.viewModel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.orlinskas.notebook.App.Component.app
import com.orlinskas.notebook.Enums
import com.orlinskas.notebook.builder.ToastBuilder
import com.orlinskas.notebook.interactor.*
import com.orlinskas.notebook.interactor.BaseUseCase.NULL
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
    @Inject lateinit var updateUseCase: UpdateDataUseCase
    @Inject lateinit var syncDataUseCase: SyncDataUseCase
    @Inject lateinit var context: Context

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
                ToastBuilder.doToast(context, e.message.toString())
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
        updateUseCase(params = System.currentTimeMillis()) {
            try {
                daysData.postValue(it.data as List<Day>?)
                handleConnection(it.code)
            } catch (e: Exception) {
                ToastBuilder.doToast(context, e.message.toString())
            }
        }
    }

    private fun syncData() {
        syncDataUseCase(params = NULL()) {
            if(it.data as Boolean) {
                updateDaysData()
                ToastBuilder.doToast(context, "Синхронизировано")
            }
            else {
                ToastBuilder.doToast(context, "Не синхронизировано с сервером")
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