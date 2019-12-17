package com.orlinskas.notebook.server

import com.orlinskas.notebook.entity.Notification

interface ApiResponsibleListener {
    fun onDoneNotificationsResponse(list: List<Notification>)
    fun onFailResponse(message: String)
}