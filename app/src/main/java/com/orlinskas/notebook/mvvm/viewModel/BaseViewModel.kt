package com.orlinskas.notebook.mvvm.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.orlinskas.notebook.App.Component.app
import com.orlinskas.notebook.Enums
import com.orlinskas.notebook.mvvm.model.Day
import com.orlinskas.notebook.mvvm.model.Notification
import com.orlinskas.notebook.repository.NotificationRepository
import kotlinx.coroutines.*
import javax.inject.Inject

//jff not use
class BaseViewModel : ViewModel() {
    @Inject lateinit var repository: NotificationRepository
    @Inject lateinit var downloadStatusData: MutableLiveData<Enum<Enums.DownloadStatus>>
    @Inject lateinit var connectionStatusData: MutableLiveData<Enum<Enums.ConnectionStatus>>
    @Inject lateinit var daysData: MutableLiveData<List<Day>>

    private val job: Job = Job()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    init {
        app.getComponent().inject(this)

        scope.launch {
            withContext(Dispatchers.IO) {
               // repository.findActual(System.currentTimeMillis())
            }
        }
    }

    fun createNotification(notification: Notification) {
        scope.launch {
            withContext(Dispatchers.IO) {
                repository.insert(notification)
            }
        }
    }

    fun deleteNotification(notification: Notification) {
        scope.launch {
            withContext(Dispatchers.IO) {
                repository.delete(notification)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }
}