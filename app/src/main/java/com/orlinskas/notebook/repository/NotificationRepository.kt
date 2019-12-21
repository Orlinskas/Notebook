package com.orlinskas.notebook.repository

import com.orlinskas.notebook.App
import com.orlinskas.notebook.database.MyDatabase
import com.orlinskas.notebook.entity.Notification
import com.orlinskas.notebook.service.ApiFactory
import com.orlinskas.notebook.service.response.ResponseDELETE
import com.orlinskas.notebook.service.response.ResponseGET
import com.orlinskas.notebook.service.response.ResponsePOST

class NotificationRepository {
    private val database: MyDatabase = App.getInstance().myDatabase
    private val remoteService = ApiFactory.notificationApi

    suspend fun findAll(): List<Notification> {
        val localList = database.notificationDao().findAll()

        var response = ResponseGET(404, "", emptyList())

        try {
            response = remoteService.findAll()
        } finally {
            return if (response.code == 200) {
                Synchronizer().sync(localList, response.data)
            }
            else {
                localList
            }
        }
    }

    suspend fun findActual(currentDateMillis: Long): List<Notification> {
        val localList = database.notificationDao().findActual(currentDateMillis)
        localList.removeAll { notification -> notification.is_deleted  }

        var response = ResponseGET(404, "", emptyList())

        try {
            response = remoteService.findAll()
        } finally {
            return if (response.code == 200) {
                for (notification in response.data) {
                    if (notification.startDateMillis < currentDateMillis && notification.is_deleted) {
                        response.data.remove(notification)
                    }
                }
                Synchronizer().sync(localList, response.data)
            }
            else {
                localList
            }
        }
    }

    suspend fun insert(notification: Notification) {
        var response = ResponsePOST(404, "", notification)

        try {
            response = remoteService.add(notification)
        } finally {
            if (response.code == 201) {
                val remoteNotification = response.data
                remoteNotification.isSynchronized = true
                database.notificationDao().insertAll(remoteNotification)
            }
            else {
                notification.isSynchronized = false
                database.notificationDao().insertAll(notification)
            }
        }
    }

    suspend fun delete(notification: Notification) {
        var response = ResponseDELETE(404, "", notification)

        try {
            response = remoteService.delete(notification.id)
        } finally {
            if(response.code == 200) {
                database.notificationDao().delete(notification)
            }
            else {
                notification.is_deleted = true
                database.notificationDao().update(notification)
            }
        }
    }

    suspend fun sync() : Boolean { //обработать исключения!!
        val localList = database.notificationDao().findAll()

        //проверка удаления данных, нужно догнать сервер
        if(localList.isEmpty()) {
            val response = remoteService.findAll()

            if(response.code == 200 && response.data.isNotEmpty()) {
                for (notification in response.data) {
                    database.notificationDao().insertAll(notification)
                    return true
                }
            }
            else {
                return false
            }
        }

        //попытка сервера догнать локальную базу
        for (notification in localList) {
            if (!notification.isSynchronized) {
                val response = remoteService.add(notification)

                if (response.code == 201) {
                    database.notificationDao().insertAll(response.data)
                    database.notificationDao().delete(notification)
                }
                else {
                    return false
                }
            }

            if (notification.is_deleted) {
                val response = remoteService.delete(notification.id)

                if(response.code == 200) {
                    database.notificationDao().delete(notification)
                }
                else {
                    return false
                }
            }
        }

        return true
    }
}