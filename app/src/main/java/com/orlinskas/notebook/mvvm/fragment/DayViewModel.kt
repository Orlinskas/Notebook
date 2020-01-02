package com.orlinskas.notebook.mvvm.fragment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.orlinskas.notebook.App.Component.app
import com.orlinskas.notebook.mvvm.model.Day
import com.orlinskas.notebook.mvvm.viewModel.NotificationViewModel
import kotlinx.coroutines.*
import java.util.concurrent.CancellationException
import javax.inject.Inject

class DayViewModel(application: Application) : AndroidViewModel(application), NotificationViewModel {
    @Inject lateinit var daysData: LiveData<List<Day>>
    private val job: Job = Job()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    init {
        app.getComponent().inject(this)
    }

    override fun onCleared() {
        super.onCleared()
        scope.cancel(CancellationException())
    }
}