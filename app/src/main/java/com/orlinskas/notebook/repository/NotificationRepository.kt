package com.orlinskas.notebook.repository

import com.orlinskas.notebook.App
import com.orlinskas.notebook.database.MyDatabase
import com.orlinskas.notebook.entity.Notification
import com.orlinskas.notebook.service.ApiFactory
import com.orlinskas.notebook.service.NotificationRemoteRepository
import com.orlinskas.notebook.service.Synchronizer
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay

class NotificationRepository() {
    private val database: MyDatabase = App.getInstance().myDatabase
    private val remoteService = NotificationRemoteRepository(ApiFactory.notificationApi)

    suspend fun findAll() : List<Notification> {

        val local = GlobalScope.async {
            database.notificationDao().findAll()
        }

        val remote = GlobalScope.async {
            remoteService.findAll()
        }

        return Synchronizer().sync(local.await(), remote.await()!!)
    }

    fun add(notification: Notification) {
        database.notificationDao().insertAll(notification)
        remoteService.add(notification)
    }

    fun delete(notification: Notification) {
        database.notificationDao().delete(notification)
        remoteService.delete(notification)
    }
}