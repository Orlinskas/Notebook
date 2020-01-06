package com.orlinskas.notebook.di.module

import android.content.Context
import com.orlinskas.notebook.NetworkHandler
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ContextModule::class])
class NetworkHandlerModule {

    @Provides
    @Singleton
    fun provideNetworkHandler(context: Context): NetworkHandler {
        return NetworkHandler(context)
    }
}