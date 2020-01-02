package com.orlinskas.notebook.di.module

import com.orlinskas.notebook.Constants.*
import com.orlinskas.notebook.service.NotificationApiService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


@Module
class ApiModule() {

    @Provides
    fun provideNotificationApi(retrofit: Retrofit): NotificationApiService {
        return retrofit.create(NotificationApiService::class.java)
    }

    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            //.addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    fun provideClient(): OkHttpClient {
        val httpClient = OkHttpClient.Builder()

        httpClient.connectTimeout(OK_HTTP_TIMEOUT.toLong(), TimeUnit.SECONDS)
        httpClient.readTimeout(OK_HTTP_TIMEOUT.toLong(), TimeUnit.SECONDS)

        httpClient.addInterceptor { chain ->
            val request = chain.request().newBuilder().addHeader("User-Id", DEFAULT_USER_ID).build()
            chain.proceed(request)
        }

        return httpClient.build()
    }

}