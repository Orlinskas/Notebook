package com.orlinskas.notebook.service

import com.orlinskas.notebook.Constants.BASE_URL
import com.orlinskas.notebook.Constants.NOTIFICATION_PATH
import com.orlinskas.notebook.entity.Notification
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface NotificationApiService {
    @GET(NOTIFICATION_PATH)
    suspend fun findAll(): List<Notification>

    @PUT("notification{id}")
    suspend fun add(@Path("id") id: Int): Notification

    @DELETE("notification{id}")
    suspend fun delete(@Path("id") id: Int): Notification
}

private fun <E> List<E>.await(): List<E> {
    //перешел на версию retroFit 2.6.0
    return emptyList()
}
