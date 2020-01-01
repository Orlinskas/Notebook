package com.orlinskas.notebook.mvvm.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.orlinskas.notebook.App
import com.orlinskas.notebook.Enums
import com.orlinskas.notebook.mvvm.model.Day
import com.orlinskas.notebook.repository.NotificationRepository
import kotlinx.coroutines.*
import javax.inject.Inject

class MainViewModel : ViewModel(), NotificationViewModel {
    @Inject lateinit var repository: NotificationRepository
    @Inject lateinit var downloadStatusData: LiveData<Enum<Enums.DownloadStatus>>
    @Inject lateinit var connectionStatusData: LiveData<Enum<Enums.ConnectionStatus>>
    @Inject lateinit var daysData: LiveData<List<Day>>
    private val job: Job = Job()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    init {
        App().getComponent().inject(this)

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

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }
}
