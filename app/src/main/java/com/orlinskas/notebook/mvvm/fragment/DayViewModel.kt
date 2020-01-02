package com.orlinskas.notebook.mvvm.fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.orlinskas.notebook.App.Component.app
import com.orlinskas.notebook.mvvm.model.Day
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import java.util.concurrent.CancellationException
import javax.inject.Inject

class DayViewModel : ViewModel() {
    @Inject lateinit var daysData: MutableLiveData<List<Day>>
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