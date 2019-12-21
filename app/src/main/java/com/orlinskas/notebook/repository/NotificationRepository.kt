package com.orlinskas.notebook.repository

import android.util.Log
import com.orlinskas.notebook.App
import com.orlinskas.notebook.Constants.*
import com.orlinskas.notebook.database.MyDatabase
import com.orlinskas.notebook.entity.Notification
import com.orlinskas.notebook.service.ApiFactory
import com.orlinskas.notebook.service.NotificationRemoteRepository
import com.orlinskas.notebook.service.Synchronizer

class NotificationRepository {
    private val database: MyDatabase = App.getInstance().myDatabase
    private val remoteService = NotificationRemoteRepository(ApiFactory.notificationApi)

    suspend fun findAll(): List<Notification> {
        val local = try {
            database.notificationDao().findAll()
        } catch (e: Exception) {
            Log.e(javaClass.name, DATA_BASE_FAIL)
        }

        val remote = try {
            remoteService.findAll()
        } catch (e: Exception) {
            Log.e(javaClass.name, SERVER_FAIL)
        }

        return Synchronizer().sync(local, remote)
    }

    suspend fun findActual(currentDateMillis: Long): List<Notification> {
        val local = try {
            database.notificationDao().findActual(currentDateMillis)
        } catch (e: Exception) {
            Log.e(javaClass.name, DATA_BASE_FAIL)
        }

        val remote = try {
            remoteService.findActual(currentDateMillis)
        } catch (e: Exception) {
            Log.e(javaClass.name, SERVER_FAIL)
        }

        return Synchronizer().sync(local, remote)
    }

    suspend fun insertAll(vararg notifications: Notification?) {
        try {
            database.notificationDao().insertAll(*notifications)
            Log.v(javaClass.name, "-БД- Добавленно- -- ${notifications.size} -- объектов")
        } catch (e: Exception) {
            Log.e(javaClass.name, DATA_BASE_FAIL)
        }

        try {
            remoteService.insertAll(*notifications)
        } catch (e: Exception) {
            Log.e(javaClass.name, SERVER_FAIL)
        }
    }

    suspend fun delete(notification: Notification) {
        try {
            database.notificationDao().delete(notification)
            Log.v(javaClass.name, "-БД- Удален объект -- ${notification.id}")
        } catch (e: Exception) {
            Log.e(javaClass.name, DATA_BASE_FAIL)
        }

        try {
            remoteService.delete(notification)
        } catch (e: Exception) {
            Log.e(javaClass.name, SERVER_FAIL)
        }
    }

    suspend fun update(notification: Notification?) {
        try {
            database.notificationDao().update(notification)
            Log.v(javaClass.name, "-БД- Обновлен объект -- ${notification?.id}")
        } catch (e: Exception) {
            Log.e(javaClass.name, DATA_BASE_FAIL)
        }

        try {
            remoteService.update(notification)
            Log.v(javaClass.name, SERVER_DONE)
        } catch (e: Exception) {
            Log.e(javaClass.name, SERVER_FAIL)
        }
    }
}