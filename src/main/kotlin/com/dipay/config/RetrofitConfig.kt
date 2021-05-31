package com.dipay.config

import com.dipay.service.integration.AuthorizationService
import com.dipay.service.integration.NotificationService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitConfig {

    private val retrofitAuth = Retrofit.Builder()
        .baseUrl("https://run.mocky.io/v3/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val retrofitNotification = Retrofit.Builder()
        .baseUrl("http://o4d9z.mocklab.io/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun authorizationService(): AuthorizationService = retrofitAuth.create(AuthorizationService::class.java)

    fun notificationService(): NotificationService = retrofitNotification.create(NotificationService::class.java)
}
