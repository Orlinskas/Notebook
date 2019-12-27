package com.orlinskas.notebook.mvvm.fragment

import com.orlinskas.notebook.mvvm.model.Notification

interface DayFragmentActions {
    fun openDay(dayID: Int)
    fun deleteNotification(notification: Notification)
}
