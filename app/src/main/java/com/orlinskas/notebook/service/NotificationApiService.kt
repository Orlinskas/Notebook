package com.orlinskas.notebook.service

import com.orlinskas.notebook.entity.Notification
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface NotificationApiService {

    @GET("notifications/")
    suspend fun findAll() : List<Notification>

    @POST("notification{id}")
    suspend fun add(@Path("id") id: Int)

    @DELETE("notification{id}")
    suspend fun delete(@Path("id") id: Int)
}

//private fun <E> List<E>.await(): List<E> {
//    //перешел на версию retroFit 2.6.0
//    return emptyList()
//}
