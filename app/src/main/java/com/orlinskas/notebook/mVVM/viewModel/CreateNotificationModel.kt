package com.orlinskas.notebook.mVVM.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.orlinskas.notebook.App
import com.orlinskas.notebook.Enums
import com.orlinskas.notebook.mVVM.model.Notification
import kotlinx.coroutines.*

class CreateNotificationModel(application: Application) : AndroidViewModel(application) {
    private val repository = App.getInstance().repository
    val repositoryStatusData: LiveData<Enum<Enums.RepositoryStatus>> = repository.repositoryStatusData
    val connectionStatusData: LiveData<Enum<Enums.ConnectionStatus>> = repository.connectionStatusData
    private var job: Job = Job()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    fun createNotification(notification: Notification) {
       scope.launch {
           withContext(Dispatchers.IO) {
               repository.insert(notification)
           }
       }
    }

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }
}
