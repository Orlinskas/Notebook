package com.orlinskas.notebook.service

import com.orlinskas.notebook.entity.Notification

class NotificationRemoteRepository(private val apiService: NotificationApiService) {

    suspend fun findAll(): List<Notification>? {
        return apiService.findAll()
    }

    suspend fun add(notification: Notification) {
        apiService.add(notification.id)
    }

    suspend fun delete(notification: Notification) {
        apiService.delete(notification.id)
    }

}
