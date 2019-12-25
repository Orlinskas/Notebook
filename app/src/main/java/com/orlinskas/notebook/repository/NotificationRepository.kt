package com.orlinskas.notebook.repository

import androidx.lifecycle.*
import com.orlinskas.notebook.App
import com.orlinskas.notebook.activity.ConnectionCallBack
import com.orlinskas.notebook.builder.DaysBuilder
import com.orlinskas.notebook.database.MyDatabase
import com.orlinskas.notebook.entity.Notification
import com.orlinskas.notebook.service.ApiFactory
import com.orlinskas.notebook.value.Day

class NotificationRepository(connectionCallBack: ConnectionCallBack) : LifecycleObserver {
    private val database: MyDatabase = App.instance.myDatabase
    private val remoteService = ApiFactory.notificationApi
    private val synchronizer = Synchronizer()
    private val model = connectionCallBack
    private val allLiveData = App.instance.allNotificationsLiveData
    private val actualLiveData = App.instance.actualNotificationsLiveData
    private val daysData = App.instance.daysLiveData

    //@OnLifecycleEvent(Lifecycle.Event.ON_START)
    suspend fun findAll(): MutableLiveData<List<Notification>> {
        val localData = database.notificationDao().findAll()
        allLiveData.value = localData

        try {
            val response = remoteService.findAll()

            if (response.code == 200) {
                model.doneConnection()
                val syncData = synchronizer.sync(localData, response.data)
                allLiveData.value = syncData
            }
            else {
                model.failConnection()
            }
        } catch (e : Exception) {
            model.failConnection()
        }

        return allLiveData
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    suspend fun findActual(currentDateMillis: Long): MutableLiveData<List<Day>> {
        val localData = database.notificationDao().findActual(currentDateMillis)
        localData.removeAll { notification -> notification.is_deleted }
        daysData.postValue(DaysBuilder(localData).findActual())

        try {
             val response = remoteService.findAll()

             if (response.code == 200) {
                 for (notification in response.data) {
                     if (notification.startDateMillis < currentDateMillis && notification.is_deleted) {
                         response.data.remove(notification)
                     }
                 }
                 val syncData = synchronizer.sync(localData, response.data)
                 daysData.postValue(DaysBuilder(syncData).findActual())
             }
             else {
                 model.failConnection()
             }
        } catch (e : Exception){
            model.failConnection()
        }

        return daysData
    }

    suspend fun insert(notification: Notification) : MutableLiveData<List<Day>> {
        try {
            val response = remoteService.add(notification)

            if (response.code == 201) {
                val remoteNotification = response.data
                remoteNotification.isSynchronized = true
                database.notificationDao().insertAll(remoteNotification)
                model.doneConnection()
            }
            else {
                notification.isSynchronized = false
                database.notificationDao().insertAll(notification)
                model.failConnection()
            }
        } catch (e : Exception) {
            notification.isSynchronized = false
            database.notificationDao().insertAll(notification)
            model.failConnection()
        }

        return findActual(System.currentTimeMillis())
    }

    suspend fun delete(notification: Notification) : MutableLiveData<List<Day>> {
        try {
           val response = remoteService.delete(notification.id)

            if(response.code == 200) {
                database.notificationDao().delete(notification)
                model.doneConnection()
            }
            else {
                notification.is_deleted = true
                database.notificationDao().update(notification)
                model.failConnection()
            }
        } catch (e : Exception) {
            notification.is_deleted = true
            database.notificationDao().update(notification)
            model.failConnection()
        }

        return findActual(System.currentTimeMillis())
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
