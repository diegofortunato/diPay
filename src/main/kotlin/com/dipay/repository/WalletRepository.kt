package com.dipay.repository

import com.dipay.entity.WalletEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface WalletRepository : JpaRepository<WalletEntity, Long> {

    fun findByWalletOwnerID(walletOwnerId: Long): Optional<WalletEntity>
}
