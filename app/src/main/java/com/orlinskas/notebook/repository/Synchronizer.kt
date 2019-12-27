package com.orlinskas.notebook.repository

import android.util.Log
import com.orlinskas.notebook.mvvm.model.Notification

class Synchronizer {
    fun sync(localData: Any, remoteData: Any): List<Notification> {
        val syncSet = mutableSetOf<Notification>()

        Log.v(javaClass.name, "Начало синхронизации" )

        if (localData is List<*>) {
            val localList: List<Notification> = localData.filterIsInstance<Notification>()
            Log.v(javaClass.name, "Базаданных вернула -- ${localList.size} -- данных" )

            for(notification in localList) {
                syncSet.add(notification)
                Log.v(javaClass.name, "Добавлена заметка из БД -- " + notification.id)
            }
        }
        else {
            Log.v(javaClass.name, "Получены не правильные данные из БД")
        }

        if (remoteData is List<*>) {
            val remoteList: List<Notification> = remoteData.filterIsInstance<Notification>()
            Log.v(javaClass.name, "Сервер вернул -- ${remoteList.size} -- данных" )

            for(notification in remoteList) {
                syncSet.add(notification)
                Log.v(javaClass.name, "Добавлена заметка из сервера -- " + notification.id)
            }
        }
        else {
            Log.v(javaClass.name, "Получены не правильные данные из сервера")
        }

        Log.v(javaClass.name, "Синхронизировано -- ${syncSet.size} -- данных")

        return syncSet.toList()
    }

}