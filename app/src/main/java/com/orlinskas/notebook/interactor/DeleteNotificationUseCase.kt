package com.orlinskas.notebook.interactor

import com.orlinskas.notebook.Response
import com.orlinskas.notebook.mvvm.model.Notification
import com.orlinskas.notebook.repository.NotificationRepository
import javax.inject.Inject

class DeleteNotificationUseCase
@Inject constructor(var repository: NotificationRepository): BaseUseCase<Response, Notification>() {

    override suspend fun run(params: Notification): Response {
        return if(repository.delete(params)) {
            Response(null, 200)
        } else {
            Response(null, 404)
        }
    }
}