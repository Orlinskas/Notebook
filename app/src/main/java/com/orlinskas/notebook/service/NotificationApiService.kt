package com.orlinskas.notebook.service

import com.orlinskas.notebook.Constants.NOTIFICATION_PATH
import com.orlinskas.notebook.entity.Notification
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface NotificationApiService {
    @GET(NOTIFICATION_PATH)
    fun findAll(): List<Notification>

    @PUT("notification{id}")
    fun add(@Path("id") id: Int)

    @DELETE("notification{id}")
    fun delete(@Path("id") id: Int)
}

private fun <E> List<E>.await(): List<E> {
    //перешел на версию retroFit 2.6.0
    return emptyList()
}
