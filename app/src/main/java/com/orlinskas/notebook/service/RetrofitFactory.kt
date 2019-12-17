package com.orlinskas.notebook.service

import com.orlinskas.notebook.BuildConfig
import com.orlinskas.notebook.Constants.BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitFactory {

    fun retrofit(baseUrl : String) : Retrofit = Retrofit.Builder()
            //.client(client)
            //.addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl)
            .build()

    //private val client =
    //        if(BuildConfig.DEBUG){
    //            OkHttpClient().newBuilder()
    //                    .addInterceptor(authInterceptor)
    //                    .addInterceptor(loggingInterceptor)
    //                    .build()
    //        } else{
    //            OkHttpClient().newBuilder()
    //                    .addInterceptor(loggingInterceptor)
    //                    .addInterceptor(authInterceptor)
    //                    .build()
    //        }
}