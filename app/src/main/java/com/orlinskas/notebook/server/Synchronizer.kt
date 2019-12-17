package com.orlinskas.notebook.server

import com.orlinskas.notebook.entity.Notification

class Synchronizer {
    fun sync(localList: List<Notification>, remoteList: List<Notification>): List<Notification> {
        val syncSet = mutableSetOf<Notification>()

        for(notification in localList) {
            syncSet.add(notification)
        }
        for(notification in remoteList) {
            syncSet.add(notification)
        }

        return syncSet.toList()
    }

}