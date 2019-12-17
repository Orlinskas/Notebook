package com.orlinskas.notebook.server

import com.orlinskas.notebook.Constants.BASE_URL
import com.orlinskas.notebook.Constants.NOTIFICATION_PATH
import com.orlinskas.notebook.entity.Notification
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.*

interface NotificationApiService {
    @GET(NOTIFICATION_PATH)
    fun findAll(): Call <List<Notification>>

    @PUT("notification{id}")
    fun add(@Path("id") id: Int): Call<Notification>

    @DELETE("notification{id}")
    fun delete(@Path("id") id: Int): Call<Notification>

    /**
     * Companion object to create the NotificationApiService
     */
    companion object Factory {
        fun create(): NotificationApiService {
            val retrofit = Retrofit.Builder()
                    //.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BASE_URL)
                    .build()

            return retrofit.create(NotificationApiService::class.java);
        }
    }
}