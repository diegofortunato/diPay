package com.dipay.service

import com.dipay.dto.TransactionDTO

interface TransactionService {
    fun performTransaction(value: Double, userPayer: Long, userPayee: Long): TransactionDTO
}
