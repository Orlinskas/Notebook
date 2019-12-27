package com.orlinskas.notebook.mVVM.fragment

import com.orlinskas.notebook.mVVM.model.Notification

interface DayFragmentActions {
    fun openDay(dayID: Int)
    fun deleteNotification(notification: Notification)
}
