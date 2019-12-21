package com.orlinskas.notebook.service

import android.util.Log
import com.orlinskas.notebook.entity.Notification
import com.orlinskas.notebook.service.response.ResponseDELETE
import com.orlinskas.notebook.service.response.ResponseGET
import com.orlinskas.notebook.service.response.ResponsePOST

class NotificationRemoteRepository(private val apiService: NotificationApiService) {

    suspend fun findAll(): ResponseGET {
        return try {
            apiService.findAll()
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun insertAll(vararg notifications: Notification?) : List<ResponsePOST> {
        val responseList = ArrayList<ResponsePOST>()

        for (notification in notifications) {
            try {
                if (notification != null) {
                     responseList.add(apiService.add(notification))
                }
            } catch (e: Exception) {
                Log.e(javaClass.name, "Ошибка добавления на сервер -- " + (notification?.id ?: "null object"))
                Log.e(javaClass.name, e.message.toString())
            }
        }

        return responseList
    }

    suspend fun delete(notification: Notification) : ResponseDELETE {
        return try {
            apiService.delete(notification.id)
        } catch (e: Exception) {
            throw e
        }
    }
}
