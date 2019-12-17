package com.orlinskas.notebook.server

import com.orlinskas.notebook.entity.Notification

class NotificationRemoteRepository(private val apiResponsibleListener: ApiResponsibleListener,
                                   private var apiService: NotificationApiService) {

    init {
        apiService = NotificationApiService.create()
    }

    fun findAll() {
        Thread(Runnable {
            try {
                val notifications = apiService.findAll().execute().body()
                if(notifications != null) {
                    apiResponsibleListener.onDoneNotificationsResponse(notifications)
                }
                else {
                    apiResponsibleListener.onFailResponse("Fail, null object")
                }
            } catch (e: Exception) {
                apiResponsibleListener.onFailResponse(e.message.toString())
                e.printStackTrace()
            }
        }).start()
    }

    fun add(notification: Notification) {
        Thread(Runnable {
            try {
                apiService.add(notification.id)
            } catch (e: Exception) {
                apiResponsibleListener.onFailResponse("Fail to put object")
                e.printStackTrace()
            }
        })
    }

    fun delete(notification: Notification) {
        Thread(Runnable {
            try {
                apiService.delete(notification.id)
            } catch (e: Exception) {
                apiResponsibleListener.onFailResponse("Fail to put object")
                e.printStackTrace()
            }
        })
    }

    //необхоимо реализовать остальные методы
}
