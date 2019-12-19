package com.orlinskas.notebook.service

import com.orlinskas.notebook.database.NotificationDao
import com.orlinskas.notebook.entity.Notification

class NotificationRemoteRepository(private val apiService: NotificationApiService) : NotificationDao {

    override fun findAll(): List<Notification>? {
        var notifications = ArrayList<Notification>()
        try {
            notifications = apiService.findAll() as ArrayList<Notification>
        } finally {
            return notifications
        }
    }

    override fun findActual(currentDateMillis: Long): MutableList<Notification> {
        val actualNotifications = ArrayList<Notification>()

        val notifications = try {
            apiService.findAll()
        } catch (e: Exception) {
            return actualNotifications
        }

        for (notification in notifications) {
            if (notification.startDateMillis > currentDateMillis && notification.deleted_at == null) {
                actualNotifications.add(notification)
            }
        }

        return actualNotifications
    }

    override fun insertAll(vararg notifications: Notification?) {
        for (notification in notifications) {
            if (notification != null) {
                apiService.add(notification.id)
            }
        }
    }

    override fun delete(notification: Notification?) {
        if (notification != null) {
            apiService.delete(notification.id)
        }
    }

    override fun update(notification: Notification?) {
        //
    }
}
