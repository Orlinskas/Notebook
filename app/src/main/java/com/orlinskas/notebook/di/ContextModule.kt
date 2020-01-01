package com.orlinskas.notebook.di

import android.content.Context

import dagger.Module
import dagger.Provides

@Module
class ContextModule(private val context: Context) {

    @Provides
    fun context(): Context {
        return context
    }

    @ApplicationContext
    @Provides
    fun applicationContext(): Context {
        return context.applicationContext
    }
}

