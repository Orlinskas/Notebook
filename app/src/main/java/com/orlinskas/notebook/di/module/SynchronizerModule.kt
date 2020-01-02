package com.orlinskas.notebook.di.module

import com.orlinskas.notebook.repository.Synchronizer
import dagger.Module
import dagger.Provides

@Module
class SynchronizerModule {

    @Provides
    fun provideSynchronizer(): Synchronizer {
        return Synchronizer()
    }
}