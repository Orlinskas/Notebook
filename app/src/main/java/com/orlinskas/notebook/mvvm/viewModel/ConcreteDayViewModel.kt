package com.orlinskas.notebook.mvvm.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.orlinskas.notebook.Enums
import com.orlinskas.notebook.di.DaggerNotificationComponent
import com.orlinskas.notebook.mvvm.model.Day
import com.orlinskas.notebook.mvvm.model.Notification
import kotlinx.coroutines.*

class ConcreteDayViewModel : ViewModel() {
    private val component = DaggerNotificationComponent.builder().build()
    private val repository = component.notificationRepository
    val downloadStatusData: LiveData<Enum<Enums.DownloadStatus>> = component.downloadStatusData
    val connectionStatusData: LiveData<Enum<Enums.ConnectionStatus>> = component.connectionStatusData
    lateinit var daysData: LiveData<List<Day>>
    private val job: Job = Job()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    init {
        runBlocking {
            daysData = withContext(Dispatchers.IO) {
                repository.daysData
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
