package com.dipay.util.mapper

import com.dipay.dto.TransactionDTO
import com.dipay.dto.UserDTO
import com.dipay.dto.WalletDTO
import com.dipay.entity.TransactionEntity
import com.dipay.entity.UserEntity
import com.dipay.entity.WalletEntity

object EntityToDTOMapper {
    fun toUserDTO(userEntity: UserEntity) = UserDTO(
        userEntity.userId,
        userEntity.userFullName,
        userEntity.userDocument,
        userEntity.userEmail,
        userEntity.userPassword,
        userEntity.userType
    )

    fun toTransactionDTO(transactionEntity: TransactionEntity): TransactionDTO {
        return TransactionDTO(
            transactionEntity.transactionId,
            transactionEntity.transactionValue,
            transactionEntity.userPayer!!.userId,
            transactionEntity.userPayee!!.userId
        )
    }

    fun toWalletDTO(walletEntity: WalletEntity): WalletDTO {
        return WalletDTO(
            walletEntity.walletId!!,
            walletEntity.walletValue,
            walletEntity.walletOwnerID
        )
    }
}




