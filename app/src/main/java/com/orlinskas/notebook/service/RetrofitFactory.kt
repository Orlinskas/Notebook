package com.orlinskas.notebook.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitFactory {

    fun retrofit(baseUrl : String) : Retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            //.addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    private val client = Client.create()

}