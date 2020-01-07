package com.orlinskas.notebook.interactor

import com.orlinskas.notebook.Response
import com.orlinskas.notebook.repository.NotificationRepository
import javax.inject.Inject

class SyncDataUseCase
@Inject constructor(var repository: NotificationRepository): BaseUseCase<Response, BaseUseCase.NULL>() {

    override suspend fun run(params: NULL): Response {
        val isDone = repository.sync()
        return Response(isDone, 404)
    }
}