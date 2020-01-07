package com.orlinskas.notebook.di.module

import com.orlinskas.notebook.NetworkHandler
import com.orlinskas.notebook.interactor.*
import com.orlinskas.notebook.repository.NotificationRepository
import com.orlinskas.notebook.repository.Synchronizer
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [RepositoryModule::class, NetworkHandlerModule::class, SynchronizerModule::class])
class InteractorModule {
    @Provides
    @Singleton
    fun provideCreateNotificationUseCase(repository: NotificationRepository): CreateNotificationUseCase {
        return CreateNotificationUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideDeleteNotificationUseCase(repository: NotificationRepository): DeleteNotificationUseCase {
        return DeleteNotificationUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideFindActualNotificationUseCase(repository: NotificationRepository,
                                             networkHandler: NetworkHandler,
                                             synchronizer: Synchronizer): FindActualNotificationUseCase {

        return FindActualNotificationUseCase(repository,synchronizer, networkHandler)
    }

    @Provides
    @Singleton
    fun provideSyncDataUseCase(repository: NotificationRepository): SyncDataUseCase {
        return SyncDataUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideUpdateDataUseCase(repository: NotificationRepository): UpdateDataUseCase {
        return UpdateDataUseCase(repository)
    }
}