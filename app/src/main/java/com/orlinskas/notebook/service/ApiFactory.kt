package com.orlinskas.notebook.service

import com.orlinskas.notebook.Constants.BASE_URL

object ApiFactory {

    val notificationApi : NotificationApiService = RetrofitFactory.retrofit(BASE_URL)
            .create(NotificationApiService::class.java)

    //val someOtherApi : OtherApi
}