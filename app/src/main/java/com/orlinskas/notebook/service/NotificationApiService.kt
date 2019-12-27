package com.orlinskas.notebook.service

import com.orlinskas.notebook.MVVM.model.Notification
import com.orlinskas.notebook.service.response.ResponseDELETE
import com.orlinskas.notebook.service.response.ResponseGET
import com.orlinskas.notebook.service.response.ResponsePOST
import retrofit2.http.*


interface NotificationApiService {

    @GET("notifications/")
    suspend fun findAll() : ResponseGET

    @POST("notifications/")
    suspend fun add(@Body notification: Notification) : ResponsePOST

    @DELETE("notifications/{id}")
    suspend fun delete(@Path("id") id: Int) : ResponseDELETE
}

