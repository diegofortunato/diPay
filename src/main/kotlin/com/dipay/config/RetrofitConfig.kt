package com.dipay.config

import com.dipay.service.integration.AuthorizationService
import com.dipay.service.integration.NotificationService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitConfig {

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(20, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofitAuth = Retrofit.Builder()
        .baseUrl("https://run.mocky.io/v3/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    private val retrofitNotification = Retrofit.Builder()
        .baseUrl("http://o4d9z.mocklab.io/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    fun authorizationService(): AuthorizationService = retrofitAuth.create(AuthorizationService::class.java)

    fun notificationService(): NotificationService = retrofitNotification.create(NotificationService::class.java)
}
