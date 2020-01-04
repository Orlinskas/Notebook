package com.orlinskas.notebook.interactor

import com.orlinskas.notebook.builder.DaysBuilder
import com.orlinskas.notebook.repository.NotificationRepository
import com.orlinskas.notebook.repository.Synchronizer
import kotlinx.coroutines.*
import javax.inject.Inject

class FindActualNotificationUseCase : Interactor {
    @Inject internal lateinit var repository: NotificationRepository
    @Inject internal lateinit var synchronizer: Synchronizer

    private val job: Job = Job()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    override fun execute() {
        val currentDateMillis = System.currentTimeMillis()

        scope.launch {
            val local = async {
                repository.findActualLocal(currentDateMillis)
            }

            daysData.postValue(DaysBuilder(local.await()).findActual())

            val remote = async {
                repository.findActualRemote(currentDateMillis)
            }

            daysData.postValue(DaysBuilder(synchronizer.sync(local.await(), remote.await())).findActual())
        }
    }
}