package com.dipay.service.impl

import com.dipay.config.RetrofitConfig
import com.dipay.constant.APIConstant
import com.dipay.exception.TransactionException
import org.springframework.stereotype.Service

@Service
class NotificationServiceImpl {

    fun notificationUser() {
        val call = RetrofitConfig().notificationService().notificationUser()
        val message = call.execute().body()!!.message
        if (message != APIConstant.NOTIFICATION_SUCCESS_MESSAGE)
            throw TransactionException(APIConstant.USER_TRANSACTION_NOT_NOTIFICATION)
    }
}