package com.orlinskas.notebook.interactor

import androidx.lifecycle.MutableLiveData
import com.orlinskas.notebook.App
import com.orlinskas.notebook.Enums
import com.orlinskas.notebook.mvvm.model.Day
import com.orlinskas.notebook.repository.NotificationRepository
import com.orlinskas.notebook.repository.Synchronizer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import javax.inject.Inject

open class BaseInteractor {
    @Inject internal lateinit var daysData: MutableLiveData<List<Day>>
    @Inject internal lateinit var downloadStatusData: MutableLiveData<Enum<Enums.DownloadStatus>>
    @Inject internal lateinit var connectionStatusData: MutableLiveData<Enum<Enums.ConnectionStatus>>
    @Inject internal lateinit var repository: NotificationRepository
    @Inject internal lateinit var synchronizer: Synchronizer

    private val job: Job = Job()
    internal val scope = CoroutineScope(Dispatchers.IO + job)

    init {
        App.app.getComponent().inject(this)
    }
}