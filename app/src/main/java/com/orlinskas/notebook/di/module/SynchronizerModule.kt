package com.orlinskas.notebook.di.module

import com.orlinskas.notebook.repository.Synchronizer
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class SynchronizerModule {

    @Provides
    @Singleton
    fun provideSynchronizer(): Synchronizer {
        return Synchronizer()
    }
}