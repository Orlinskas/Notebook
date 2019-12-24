package com.orlinskas.notebook.repository

import com.orlinskas.notebook.App
import com.orlinskas.notebook.activity.ViewModel
import com.orlinskas.notebook.database.MyDatabase
import com.orlinskas.notebook.entity.Notification
import com.orlinskas.notebook.service.ApiFactory
import kotlinx.coroutines.suspendCancellableCoroutine

class NotificationRepository(viewModel: ViewModel) {
    private val database: MyDatabase = App.getInstance().myDatabase
    private val remoteService = ApiFactory.notificationApi
    private val synchronizer = Synchronizer()
    private val model = viewModel

    suspend fun findAll(): List<Notification> {
        val localData = database.notificationDao().findAll()

        return try {
            val response = remoteService.findAll()

            if (response.code == 200) {
                model.doneConnection()
                synchronizer.sync(localData, response.data)
            }
            else {
                model.failConnection()
                localData
            }
        } catch (e : Exception) {
            model.failConnection()
            localData
        }
    }

    suspend fun findActual(currentDateMillis: Long): List<Notification> {
        val localData = database.notificationDao().findActual(currentDateMillis)
        localData.removeAll { notification -> notification.is_deleted  }

        return try {
             val response = remoteService.findAll()

             if (response.code == 200) {
                 for (notification in response.data) {
                     if (notification.startDateMillis < currentDateMillis && notification.is_deleted) {
                         response.data.remove(notification)
                     }
                 }
                 synchronizer.sync(localData, response.data)
             }
             else {
                 localData
             }
        } catch (e : Exception){
            localData
        }
    }

    suspend fun insert(notification: Notification) {
        try {
            val response = remoteService.add(notification)

            if (response.code == 201) {
                val remoteNotification = response.data
                remoteNotification.isSynchronized = true
                database.notificationDao().insertAll(remoteNotification)
            }
            else {
                notification.isSynchronized = false
                database.notificationDao().insertAll(notification)
            }
        } catch (e : Exception) {
            notification.isSynchronized = false
            database.notificationDao().insertAll(notification)
        }
    }

    suspend fun delete(notification: Notification) {
        try {
           val response = remoteService.delete(notification.id)

            if(response.code == 200) {
                database.notificationDao().delete(notification)
                model.doneConnection()
                model.updateUI()
            }
            else {
                notification.is_deleted = true
                database.notificationDao().update(notification)
                model.failConnection()
                model.updateUI()
            }
        } catch (e : Exception) {
            notification.is_deleted = true
            database.notificationDao().update(notification)
            model.failConnection()
            model.updateUI()
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