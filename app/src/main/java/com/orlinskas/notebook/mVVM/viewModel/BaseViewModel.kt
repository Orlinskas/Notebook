package com.orlinskas.notebook.mVVM.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.orlinskas.notebook.App
import com.orlinskas.notebook.Enums
import com.orlinskas.notebook.mVVM.model.Day
import com.orlinskas.notebook.mVVM.model.Notification
import kotlinx.coroutines.*

//jff not use
class BaseViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = App.getInstance().repository
    val repositoryStatusData: LiveData<Enum<Enums.RepositoryStatus>> = repository.repositoryStatusData
    val connectionStatusData: LiveData<Enum<Enums.ConnectionStatus>> = repository.connectionStatusData
    lateinit var daysData: LiveData<List<Day>>
    private val job: Job = Job()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    init {
        runBlocking {
            daysData = withContext(Dispatchers.IO) {
                repository.daysData
            }
        }

        scope.launch {
            withContext(Dispatchers.IO) {
                repository.findActual(System.currentTimeMillis())
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