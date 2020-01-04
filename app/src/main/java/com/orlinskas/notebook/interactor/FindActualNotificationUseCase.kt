package com.orlinskas.notebook.interactor

import com.orlinskas.notebook.Response
import com.orlinskas.notebook.builder.DaysBuilder
import com.orlinskas.notebook.repository.NotificationRepository
import com.orlinskas.notebook.repository.Synchronizer
import kotlinx.coroutines.*
import javax.inject.Inject

class FindActualNotificationUseCase(val iteractor: IteractorCallBack) : Interactor {
    @Inject internal lateinit var repository: NotificationRepository
    @Inject internal lateinit var synchronizer: Synchronizer

    private val job: Job = Job()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    override fun run() {
        val currentDateMillis = System.currentTimeMillis()

        val response = Response(null, 404)

        scope.launch {
            val local = async {
                repository.findActualLocal(currentDateMillis)
            }

            response.data = DaysBuilder(local.await()).findActual()
            iteractor.callBack(response)

            val remote = async {
                repository.findActualRemote(currentDateMillis)
            }

            response.data = DaysBuilder(synchronizer.sync(local.await(), remote.await())).findActual()
            response.code = 200
            iteractor.callBack(response)
        }
    }
}