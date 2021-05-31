package com.dipay.service.integration

import com.dipay.dto.MessageDTO
import retrofit2.Call
import retrofit2.http.GET

interface AuthorizationService {
    @GET("8fafdd68-a090-496f-8c9a-3442cf30dae6")
    fun authUser(): Call<MessageDTO>
}
