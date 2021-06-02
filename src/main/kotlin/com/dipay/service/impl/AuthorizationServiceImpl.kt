package com.dipay.service.impl

import com.dipay.config.RetrofitConfig
import com.dipay.constant.APIConstant
import com.dipay.exception.TransactionException
import org.springframework.stereotype.Service

@Service
class AuthorizationServiceImpl {

    fun verifyUserAuth() {
        val call = RetrofitConfig().authorizationService().authUser()
        val message = call.execute().body()!!.message
        if (message != APIConstant.AUTH_SUCCESS_MESSAGE)
            throw TransactionException(APIConstant.USER_TRANSACTION_NOT_AUTHORIZED)
    }
}
