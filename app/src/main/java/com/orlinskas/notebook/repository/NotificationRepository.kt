package com.orlinskas.notebook.repository

import com.orlinskas.notebook.App
import com.orlinskas.notebook.database.MyDatabase
import com.orlinskas.notebook.entity.Notification
import com.orlinskas.notebook.service.ApiFactory
import com.orlinskas.notebook.service.NotificationRemoteRepository
import com.orlinskas.notebook.service.Synchronizer
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.future.future
import java.util.concurrent.CompletableFuture

class NotificationRepository {
    private val database: MyDatabase = App.getInstance().myDatabase
    private val remoteService = NotificationRemoteRepository(ApiFactory.notificationApi)

    fun findAll() : CompletableFuture<List<Notification>> = GlobalScope.future {
        val local = GlobalScope.async {
            database.notificationDao().findAll()
        }

        //val remote = GlobalScope.async {
        //    remoteService.findAll()
        //}

        return@future Synchronizer().sync(local.await(), local.await()!!)
    }

    suspend fun add(notification: Notification) : Boolean {
        val local = GlobalScope.async {
            database.notificationDao().insertAll(notification)
        }

        val remote = GlobalScope.async {
            remoteService.add(notification)
        }

        return remote.await()
    }

    fun delete(notification: Notification) : CompletableFuture<Boolean> = GlobalScope.future {
        val local = GlobalScope.async {
            database.notificationDao().delete(notification)
        }

        val remote = GlobalScope.async {
            remoteService.delete(notification)
        }

        return@future remote.await()
    }
}