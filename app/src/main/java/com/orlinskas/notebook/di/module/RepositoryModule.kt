package com.orlinskas.notebook.di.module

import com.orlinskas.notebook.database.MyDatabase
import com.orlinskas.notebook.repository.NotificationRepository
import com.orlinskas.notebook.repository.Synchronizer
import com.orlinskas.notebook.service.NotificationApiService
import dagger.Module
import dagger.Provides

@Module(includes = [DatabaseModule::class, ApiModule::class, SynchronizerModule::class])
class RepositoryModule {

    @Provides
    fun provideRepository(database: MyDatabase, apiService: NotificationApiService, synchronizer: Synchronizer): NotificationRepository {
        return NotificationRepository(database, apiService, synchronizer)
    }
}