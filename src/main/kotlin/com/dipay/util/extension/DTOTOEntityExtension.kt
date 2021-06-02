package com.dipay.util.extension

import com.dipay.dto.UserDTO
import com.dipay.dto.WalletDTO
import com.dipay.entity.UserEntity
import com.dipay.entity.WalletEntity

object DTOTOEntityExtension {

    fun UserDTO.toEntity() = UserEntity(
        this.userId,
        this.userFullName,
        this.userDocument,
        this.userEmail,
        this.userPassword,
        this.userType,
        null,
        null,
        null
    )

    fun WalletDTO.toEntity() = WalletEntity(
        this.walletId,
        this.walletValue,
        this.walletOwner
    )
}
