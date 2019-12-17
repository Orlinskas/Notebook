package com.orlinskas.notebook.server

import com.orlinskas.notebook.entity.Notification

class NotificationRemoteRepository() {
    private var apiService = NotificationApiService.create()

    fun findAll(): List<Notification>? {
        return apiService.findAll().execute().body()
    }

    fun add(notification: Notification) {
        Thread(Runnable {
            try {
                apiService.add(notification.id)
            } catch (e: Exception) {
                //apiResponsibleListener.onFailResponse("Fail to put object")
                e.printStackTrace()
            }
        })
    }

    fun delete(notification: Notification) {
        Thread(Runnable {
            try {
                apiService.delete(notification.id)
            } catch (e: Exception) {
                //apiResponsibleListener.onFailResponse("Fail to put object")
                e.printStackTrace()
            }
        })
    }

    //необхоимо реализовать остальные методы
}
