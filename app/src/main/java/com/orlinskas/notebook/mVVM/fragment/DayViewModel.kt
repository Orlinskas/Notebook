package com.orlinskas.notebook.mVVM.fragment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.orlinskas.notebook.App
import com.orlinskas.notebook.Enums
import com.orlinskas.notebook.mVVM.model.Day
import kotlinx.coroutines.*
import java.util.concurrent.CancellationException

class DayViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = App.getInstance().repository
    val repositoryStatusData: LiveData<Enum<Enums.RepositoryStatus>> = repository.repositoryStatusData
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

    override fun onCleared() {
        super.onCleared()
        scope.cancel(CancellationException())
    }
}