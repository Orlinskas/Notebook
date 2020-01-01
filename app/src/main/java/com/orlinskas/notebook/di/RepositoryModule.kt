package com.orlinskas.notebook.di

import android.content.Context

import androidx.room.Room

import com.orlinskas.notebook.database.MyDatabase
import com.orlinskas.notebook.repository.NotificationRepository
import com.orlinskas.notebook.repository.Synchronizer
import com.orlinskas.notebook.service.ApiFactory
import com.orlinskas.notebook.service.NotificationApiService

import javax.inject.Singleton

import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {
    @Provides
    fun notificationRepository(database: MyDatabase, apiService: NotificationApiService, synchronizer: Synchronizer): NotificationRepository {
        return NotificationRepository(database, apiService, synchronizer)
    }

    @Provides
    fun database(context: Context): MyDatabase {
        return Room.databaseBuilder(context, MyDatabase::class.java, "notification").build()
    }

    @Provides
    fun notificationApiService(): NotificationApiService {
        return ApiFactory.notificationApi
    }

    @Provides
    fun synchronizer(): Synchronizer {
        return Synchronizer()
    }
}
