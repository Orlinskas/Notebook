package com.orlinskas.notebook.repository

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import com.orlinskas.notebook.App
import com.orlinskas.notebook.Enums
import com.orlinskas.notebook.builder.DaysBuilder
import com.orlinskas.notebook.database.MyDatabase
import com.orlinskas.notebook.entity.Notification
import com.orlinskas.notebook.service.ApiFactory
import com.orlinskas.notebook.value.Day

class NotificationRepository : LifecycleObserver {
    private val database: MyDatabase = App.instance.myDatabase
    private val remoteService = ApiFactory.notificationApi
    private val synchronizer = Synchronizer()
    private val notificationsData = App.instance.allNotificationsLiveData
    val daysData: MutableLiveData<MutableList<Day>> = App.instance.daysLiveData
    val repositoryStatusData: MutableLiveData<Enum<Enums.RepositoryStatus>> = App.instance.repositoryStatusData
    val connectionStatusData: MutableLiveData<Enum<Enums.ConnectionStatus>> = App.instance.connectionStatusData

    suspend fun fastStart(): MutableLiveData<MutableList<Day>> {
        repositoryStatusData.postValue(Enums.RepositoryStatus.LOADING)

        val localData = database.notificationDao().findActual(System.currentTimeMillis())
        localData.removeAll { notification -> notification.is_deleted }
        daysData.postValue(DaysBuilder(localData).findActual())

        repositoryStatusData.postValue(Enums.RepositoryStatus.READY)

        return daysData
    }

    //@OnLifecycleEvent(Lifecycle.Event.ON_START)
    suspend fun findAll() {
        repositoryStatusData.postValue(Enums.RepositoryStatus.LOADING)

        val localData = database.notificationDao().findAll()
        notificationsData.postValue(localData)

        try {
            val response = remoteService.findAll()

            if (response.code == 200) {
                connectionStatusData.postValue(Enums.ConnectionStatus.CONNECTION_DONE)

                val syncData = synchronizer.sync(localData, response.data)
                notificationsData.postValue(syncData)
            }
            else {
                connectionStatusData.postValue(Enums.ConnectionStatus.CONNECTION_FAIL)
            }
        } catch (e : Exception) {
            connectionStatusData.postValue(Enums.ConnectionStatus.CONNECTION_FAIL)
        }

        repositoryStatusData.postValue(Enums.RepositoryStatus.READY)
    }

    suspend fun findActual(currentDateMillis: Long) {
        repositoryStatusData.postValue(Enums.RepositoryStatus.LOADING)

        val localData = database.notificationDao().findActual(currentDateMillis)
        localData.removeAll { notification -> notification.is_deleted }
        daysData.postValue(DaysBuilder(localData).findActual())

        try {
             val response = remoteService.findAll()

             if (response.code == 200) {
                 connectionStatusData.postValue(Enums.ConnectionStatus.CONNECTION_DONE)
                 for (notification in response.data) {
                     if (notification.startDateMillis < currentDateMillis && notification.is_deleted) {
                         response.data.remove(notification)
                     }
                 }
                 val syncData = synchronizer.sync(localData, response.data)
                 daysData.postValue(DaysBuilder(syncData).findActual())
             }
             else {
                 connectionStatusData.postValue(Enums.ConnectionStatus.CONNECTION_FAIL)
             }
        } catch (e : Exception){
            connectionStatusData.postValue(Enums.ConnectionStatus.CONNECTION_FAIL)
        }

        repositoryStatusData.postValue(Enums.RepositoryStatus.READY)
    }

    suspend fun insert(notification: Notification) {
        repositoryStatusData.postValue(Enums.RepositoryStatus.LOADING)

        try {
            val response = remoteService.add(notification)

            if (response.code == 201) {
                connectionStatusData.postValue(Enums.ConnectionStatus.CONNECTION_DONE)

                val remoteNotification = response.data
                remoteNotification.isSynchronized = true
                database.notificationDao().insertAll(remoteNotification)
            }
            else {
                notification.isSynchronized = false
                database.notificationDao().insertAll(notification)
                connectionStatusData.postValue(Enums.ConnectionStatus.CONNECTION_FAIL)
            }
        } catch (e : Exception) {
            notification.isSynchronized = false
            database.notificationDao().insertAll(notification)
            connectionStatusData.postValue(Enums.ConnectionStatus.CONNECTION_FAIL)
        }

        findActual(System.currentTimeMillis())

        repositoryStatusData.postValue(Enums.RepositoryStatus.READY)
    }

    suspend fun delete(notification: Notification) {
        repositoryStatusData.postValue(Enums.RepositoryStatus.LOADING)

        try {
           val response = remoteService.delete(notification.id)

            if(response.code == 200) {
                connectionStatusData.postValue(Enums.ConnectionStatus.CONNECTION_DONE)

                database.notificationDao().delete(notification)
            }
            else {
                connectionStatusData.postValue(Enums.ConnectionStatus.CONNECTION_FAIL)

                notification.is_deleted = true
                database.notificationDao().update(notification)
            }
        } catch (e : Exception) {
            connectionStatusData.postValue(Enums.ConnectionStatus.CONNECTION_FAIL)

            notification.is_deleted = true
            database.notificationDao().update(notification)
        }

        findActual(System.currentTimeMillis())

        repositoryStatusData.postValue(Enums.RepositoryStatus.READY)
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
