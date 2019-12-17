package com.orlinskas.notebook.repository

import com.orlinskas.notebook.App
import com.orlinskas.notebook.entity.Notification
import com.orlinskas.notebook.server.ApiResponsibleListener
import com.orlinskas.notebook.server.NotificationApiService
import com.orlinskas.notebook.server.NotificationRemoteRepository
import com.orlinskas.notebook.server.Synchronizer

class NotificationRepository(private val apiResponsibleListener: ApiResponsibleListener) {
    fun findAll() : List<Notification> {
        val database = App.getInstance().myDatabase
        val localNotifications = database.notificationDao().findAll()

        val remoteService = NotificationRemoteRepository()
        val remoteNotification = remoteService.findAll()

        return if (remoteNotification != null) {
            Synchronizer().sync(localNotifications, remoteNotification)
        }
        else {
            localNotifications
        }
    }
}