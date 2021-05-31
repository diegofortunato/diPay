package com.dipay.service

import com.dipay.dto.WalletDTO
import com.dipay.entity.WalletEntity

interface WalletService {
    fun addValueToWallet(walletEntity: WalletEntity): WalletDTO
}
