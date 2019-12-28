package com.orlinskas.notebook.mvvm.fragment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.orlinskas.notebook.Enums
import com.orlinskas.notebook.di.DaggerNotificationComponent
import com.orlinskas.notebook.mvvm.model.Day
import kotlinx.coroutines.*
import java.util.concurrent.CancellationException

class DayViewModel(application: Application) : AndroidViewModel(application) {
    private val component = DaggerNotificationComponent.builder().build()
    lateinit var daysData: LiveData<List<Day>>
    private val job: Job = Job()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    init {
        runBlocking {
            daysData = withContext(Dispatchers.IO) {
                component.daysData
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        scope.cancel(CancellationException())
    }
}