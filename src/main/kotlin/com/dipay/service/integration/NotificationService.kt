package com.dipay.service.integration

import com.dipay.dto.MessageDTO
import retrofit2.Call
import retrofit2.http.GET

interface NotificationService {
    @GET("notify")
    fun notificationUser(): Call<MessageDTO>
}
