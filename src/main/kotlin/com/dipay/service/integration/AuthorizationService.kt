package com.dipay.service.integration

import com.dipay.constant.APIConstant
import com.dipay.dto.MessageDTO
import retrofit2.Call
import retrofit2.http.GET

interface AuthorizationService {
    @GET(APIConstant.AUTH_INTEGRATION)
    fun authUser(): Call<MessageDTO>
}
