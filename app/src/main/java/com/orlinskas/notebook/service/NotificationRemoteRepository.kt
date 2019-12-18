package com.orlinskas.notebook.service

import com.orlinskas.notebook.entity.Notification

class NotificationRemoteRepository(private val apiService: NotificationApiService) {

    fun findAll(): List<Notification>? {
        return apiService.findAll()
    }

    fun add(notification: Notification) : Boolean {
        return try {
            apiService.add(notification.id)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun delete(notification: Notification) : Boolean {
        return try {
            apiService.delete(notification.id)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
