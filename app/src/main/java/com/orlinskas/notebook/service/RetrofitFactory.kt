package com.orlinskas.notebook.service

import com.orlinskas.notebook.Constants.USER_ID
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException


object RetrofitFactory {

    fun retrofit(baseUrl : String) : Retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            //.addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    private val client = Client.create()

}