package com.orlinskas.notebook.interactor

import com.orlinskas.notebook.NetworkHandler
import com.orlinskas.notebook.Response
import com.orlinskas.notebook.builder.DaysBuilder
import com.orlinskas.notebook.repository.NotificationRepository
import com.orlinskas.notebook.repository.Synchronizer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import javax.inject.Inject

class FindActualNotificationUseCase
@Inject constructor(var repository: NotificationRepository, var synchronizer: Synchronizer,
                    private var networkHandler: NetworkHandler): BaseUseCase() {

    private val job: Job = Job()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    override suspend fun run(): Response {
        val currentDateMillis = System.currentTimeMillis()

        if (networkHandler.isConnected) {
            try {
                val local = scope.async {
                    repository.findActualLocal(currentDateMillis)
                }

                val remote = scope.async {
                    repository.findActualRemote(currentDateMillis)
                }

                val syncList = DaysBuilder(synchronizer.sync(local.await(), remote.await())).findActual()

                return Response(syncList, 200)

            } catch (e: Exception) {
                return Response(null, 404)
            }
        } else {
            val local = scope.async {
                repository.findActualLocal(currentDateMillis)
            }

            val localList = DaysBuilder(local.await()).findActual()

            return Response(localList, 404)
        }
    }
}