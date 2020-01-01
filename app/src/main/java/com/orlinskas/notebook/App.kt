package com.orlinskas.notebook

import android.app.Application
import com.orlinskas.notebook.di.AppComponent
import com.orlinskas.notebook.di.RepositoryModule
//import com.orlinskas.notebook.di.DaggerNotificationComponent

class App : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        //buildComponent()
    }

    fun getComponent() : AppComponent = appComponent

    //private fun buildComponent(): AppComponent {
    //     return DaggerNotificationComponent.builder()
    //             .notificationModule(RepositoryModule())
    //             .build()
    //}
}