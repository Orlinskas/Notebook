package com.orlinskas.notebook.di.module

import android.content.Context

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ContextModule(internal var context: Context) {

    @Provides
    @Singleton
    fun provideContext(): Context {
        return context.applicationContext
    }
}

