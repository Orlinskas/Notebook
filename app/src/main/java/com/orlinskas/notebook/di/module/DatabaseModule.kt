package com.orlinskas.notebook.di.module

import android.content.Context
import androidx.room.Room
import com.orlinskas.notebook.database.MyDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ContextModule::class])
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(context: Context): MyDatabase {
        return Room.databaseBuilder(context, MyDatabase::class.java, "notification").build()
    }
}