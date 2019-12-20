package com.orlinskas.notebook.service

import android.util.Log
import com.orlinskas.notebook.entity.Notification

class NotificationRemoteRepository(private val apiService: NotificationApiService) {

    suspend fun findAll(): List<Notification>? {
        return try {
            apiService.findAll()
        } catch (e: Exception) {
            throw Exception()
        }
    }

    suspend fun findActual(currentDateMillis: Long): MutableList<Notification> {
        val actualNotifications = ArrayList<Notification>()

        val notifications = try {
            apiService.findAll()
        } catch (e: Exception) {
            throw Exception()
        }

        for (notification in notifications) {
            if (notification.startDateMillis > currentDateMillis && notification.deleted_at == null) {
                actualNotifications.add(notification)
            }
        }

        return actualNotifications
    }

    suspend fun insertAll(vararg notifications: Notification?) {
        for (notification in notifications) {
            try {
                apiService.add(notification!!.id)
            } catch (e: Exception) {
                Log.e(javaClass.name, "Ошибка добавления на сервер -- " + (notification?.id ?: "null object"))
            }
        }
    }

    suspend fun delete(notification: Notification?) {
        try {
            apiService.delete(notification!!.id)
        } catch (e: Exception) {
            Log.e(javaClass.name, "Ошибка удаления с сервера -- " + (notification?.id ?: "null object"))
        }
    }

    fun update(notification: Notification?) {
        //
    }
}
