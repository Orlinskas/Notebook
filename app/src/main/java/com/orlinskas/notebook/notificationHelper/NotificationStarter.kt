package com.orlinskas.notebook.notificationHelper

import android.content.Context
import android.os.AsyncTask
import androidx.room.Room
import com.orlinskas.notebook.App
import com.orlinskas.notebook.CustomMockObjects
import com.orlinskas.notebook.database.MyDatabase
import com.orlinskas.notebook.mvvm.model.Notification
import javax.inject.Inject

class NotificationStarter { //костыль
    lateinit var database : MyDatabase

    fun start(context: Context, id: Int) {
        database = Room.databaseBuilder(context, MyDatabase::class.java, "notification").build()

        AsyncTask.execute {
            val notification = findNotification(id)
            val notificationHomeScreenShower = NotificationHomeScreenShower()
            if (notification.createDateMillis != 0L) {
                notificationHomeScreenShower.create(context, notification)
            }
        }
    }

    private fun findNotification(id: Int): Notification {
        for (notification in database.notificationDao().findAll()) {
            if (notification.id == id) {
                return notification
            }
        }

        return CustomMockObjects.getEmptyNotification()
    }
}
