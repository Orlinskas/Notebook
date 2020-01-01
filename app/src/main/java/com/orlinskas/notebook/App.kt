package com.orlinskas.notebook

import android.app.Application
import com.orlinskas.notebook.di.*

class App : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        buildComponent()
    }

    fun getComponent() : AppComponent = appComponent

    private fun buildComponent(): AppComponent {
        return DaggerAppComponent.builder()
                .contextModule(ContextModule(context = this))
                .liveDataModule(LiveDataModule())
                .repositoryModule(RepositoryModule())
                .build()
    }
}