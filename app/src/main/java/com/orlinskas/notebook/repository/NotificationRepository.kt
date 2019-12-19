package com.orlinskas.notebook.repository

import com.orlinskas.notebook.App
import com.orlinskas.notebook.database.MyDatabase
import com.orlinskas.notebook.database.NotificationDao
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

    fun findAll(): CompletableFuture<List<Notification>> = GlobalScope.future {

        val local = GlobalScope.async {
            database.notificationDao().findAll()
        }

        val remote = GlobalScope.async {
            remoteService.findAll()
        }

        val localList = local.await()

        remote.await()?.let {
            return@future Synchronizer().sync(localList, it)
        }

        return@future localList
    }

    fun findActual(currentDateMillis: Long): CompletableFuture<List<Notification>> = GlobalScope.future {

        val local = GlobalScope.async {
            database.notificationDao().findActual(currentDateMillis)
        }

        val remote = GlobalScope.async {
            remoteService.findActual(currentDateMillis)
        }

        return@future Synchronizer().sync(local.await(), remote.await())

    }

    fun insertAll(vararg notifications: Notification?) = GlobalScope.future {
        val local = GlobalScope.async {
            database.notificationDao().insertAll(*notifications)
        }

        val remote = GlobalScope.async {
            remoteService.insertAll(*notifications)
        }

        local.await() ; remote.await()
    }

    fun delete(notification: Notification?) = GlobalScope.future {
        val local = GlobalScope.async {
            database.notificationDao().delete(notification)
        }

        val remote = GlobalScope.async {
            remoteService.delete(notification)
        }

        local.await() ; remote.await()
    }

    fun update(notification: Notification?) = GlobalScope.future {
        val local = GlobalScope.async {
            database.notificationDao().update(notification)
        }

        val remote = GlobalScope.async {
            remoteService.update(notification)
        }

        local.await() ; remote.await()
    }
}