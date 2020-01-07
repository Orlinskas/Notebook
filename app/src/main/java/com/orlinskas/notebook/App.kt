package com.orlinskas.notebook

import android.app.Application
import com.orlinskas.notebook.di.component.AppComponent
import com.orlinskas.notebook.di.component.DaggerAppComponent
import com.orlinskas.notebook.di.module.*

class App : Application() {
    lateinit var appComponent: AppComponent

    companion object Component {
        lateinit var app: App
    }

    override fun onCreate() {
        super.onCreate()
        appComponent = buildComponent()
        app = this
    }

    fun getComponent() : AppComponent = appComponent

    private fun buildComponent(): AppComponent {
        return DaggerAppComponent.builder()
                .contextModule(ContextModule(applicationContext))
                .liveDataModule(LiveDataModule())
                .repositoryModule(RepositoryModule())
                .synchronizerModule(SynchronizerModule())
                .networkHandlerModule(NetworkHandlerModule())
                .interactorModule(InteractorModule())
                .build()
    }
}