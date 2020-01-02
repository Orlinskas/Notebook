package com.orlinskas.notebook.di.module

import android.content.Context

import dagger.Module
import dagger.Provides

@Module
class ContextModule(internal var context: Context) {

    @Provides
    fun provideContext(): Context {
        return context.applicationContext
    }
}

