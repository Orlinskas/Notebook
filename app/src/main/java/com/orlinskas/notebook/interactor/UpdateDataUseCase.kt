package com.orlinskas.notebook.interactor

import com.orlinskas.notebook.Response
import com.orlinskas.notebook.builder.DaysBuilder
import com.orlinskas.notebook.repository.NotificationRepository
import javax.inject.Inject

class UpdateDataUseCase
@Inject constructor(var repository: NotificationRepository): BaseUseCase<Response, Long>() {

    override suspend fun run(params: Long): Response {
        val localNotifications = repository.findActualLocal(params)
        val localDays = DaysBuilder(localNotifications).findActual()

        return Response(localDays, 404)
    }
}