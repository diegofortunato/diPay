package com.dipay.util.extension

import com.dipay.dto.TransactionDTO
import com.dipay.dto.UserDTO
import com.dipay.dto.WalletDTO
import com.dipay.entity.TransactionEntity
import com.dipay.entity.UserEntity
import com.dipay.entity.WalletEntity

object EntityToDTOExtension {

    fun UserEntity.toDTO() = UserDTO(
        this.userId,
        this.userFullName,
        this.userDocument,
        this.userEmail,
        this.userPassword,
        this.userType
    )

    fun TransactionEntity.toDTO() = TransactionDTO(
        this.transactionId,
        this.transactionValue,
        this.userPayer!!.userId,
        this.userPayee!!.userId
    )

    fun WalletEntity.toDTO() = WalletDTO(
        this.walletId!!,
        this.walletValue,
        this.walletOwnerID
    )
}
