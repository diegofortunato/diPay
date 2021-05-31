package com.dipay.util.mapper

import com.dipay.dto.TransactionDTO
import com.dipay.dto.UserDTO
import com.dipay.dto.WalletDTO
import com.dipay.entity.TransactionEntity
import com.dipay.entity.UserEntity
import com.dipay.entity.WalletEntity

object DTOTOEntityMapper {

    fun toUserEntity(userDTO: UserDTO) = UserEntity(
        userDTO.userId,
        userDTO.userFullName,
        userDTO.userDocument,
        userDTO.userEmail,
        userDTO.userPassword,
        userDTO.userType,
        null,
        null,
        null
    )

    fun toTransactionEntity(transactionDTO: TransactionDTO): TransactionEntity {
        return TransactionEntity(
            transactionDTO.transactionId,
            transactionDTO.transactionValue,
            null,
            null
        )
    }

    fun toWalletEntity(walletDTO: WalletDTO): WalletEntity {
        return WalletEntity(
            walletDTO.walletId,
            walletDTO.walletValue,
            walletDTO.walletOwner
        )
    }
}
