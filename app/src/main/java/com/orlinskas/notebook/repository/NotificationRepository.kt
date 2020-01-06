package com.orlinskas.notebook.repository

import androidx.lifecycle.LifecycleObserver
import com.orlinskas.notebook.database.MyDatabase
import com.orlinskas.notebook.mvvm.model.Notification
import com.orlinskas.notebook.service.NotificationApiService

class NotificationRepository (private var database: MyDatabase,
                             private var remoteService: NotificationApiService) : LifecycleObserver {

    suspend fun findActualLocal(currentDateMillis: Long): List<Notification> {
        val localData = database.notificationDao().findActual(currentDateMillis)
        localData.removeAll { notification -> notification.is_deleted }
        return localData
    }

    suspend fun findActualRemote(currentDateMillis: Long): List<Notification> {
        try {
            val response = remoteService.findAll()

            return if (response.code == 200) {
                for (notification in response.data) {
                    if (notification.startDateMillis < currentDateMillis && notification.is_deleted) {
                        response.data.remove(notification)
                    }
                }
                response.data
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            return emptyList()
        }
    }

    //return remote connection status
    suspend fun insert(notification: Notification): Boolean {
        try {
            val response = remoteService.add(notification)

            return if (response.code == 201) {
                val remoteNotification = response.data
                remoteNotification.isSynchronized = true
                database.notificationDao().insertAll(remoteNotification)
                true
            } else {
                notification.isSynchronized = false
                database.notificationDao().insertAll(notification)
                false
            }
        } catch (e : Exception) {
            notification.isSynchronized = false
            database.notificationDao().insertAll(notification)
            return false
        }
    }

    //return remote connection status
    suspend fun delete(notification: Notification): Boolean {
        try {
           val response = remoteService.delete(notification.id)

            return if(response.code == 200) {
                database.notificationDao().delete(notification)
                true
            } else {
                notification.is_deleted = true
                database.notificationDao().update(notification)
                false
            }
        } catch (e : Exception) {
            notification.is_deleted = true
            database.notificationDao().update(notification)
            return false
        }
    }

    suspend fun sync() : Boolean {
        val localData = database.notificationDao().findAll()

        //проверка удаления приложения
        if(localData.isEmpty()) {
            return try {
                val response = remoteService.findAll()

                if(response.code == 200 && response.data.isNotEmpty()) {
                    for (notification in response.data) {
                        database.notificationDao().insertAll(notification)
                    }
                    true
                } else {
                    false
                }
            } catch (e: Exception) {
                false
            }
        }

        //попытка сервера догнать локальную базу
        for (notification in localData) {
            if (!notification.isSynchronized) { //проверка еще недобавленных notification на сервер
                try {
                    val response = remoteService.add(notification)          //при успешном добавлении на сервер

                    if (response.code == 201) {                             //получаем новый id
                        database.notificationDao().insertAll(response.data) //и делаем подмену
                        database.notificationDao().delete(notification)     //существующей заметки
                    }
                    else {
                        return false
                    }
                } catch (e: Exception) {
                    return false
                }
            }

            if (notification.is_deleted) {
                try {
                    val response = remoteService.delete(notification.id)

                    if(response.code == 200) {
                        database.notificationDao().delete(notification)
                    }
                    else {
                        return false
                    }
                } catch (e: Exception) {
                    return false
                }
            }
        }

        return true
    }
}
