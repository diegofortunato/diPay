package com.dipay.service.impl

import com.dipay.dto.WalletDTO
import com.dipay.entity.WalletEntity
import com.dipay.repository.UserRepository
import com.dipay.repository.WalletRepository
import com.dipay.service.WalletService
import com.dipay.util.mapper.EntityToDTOMapper
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import javax.persistence.EntityNotFoundException

@Service
class WalletServiceImpl(
    private val walletRepository: WalletRepository,
    private val userRepository: UserRepository
) : WalletService {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun addValueToWallet(walletEntity: WalletEntity): WalletDTO {
        log.info("Add value to wallet service. value={}", walletEntity.walletValue)

        val walletDB = walletRepository.findById(walletEntity.walletId)

        userRepository.findById(walletEntity.walletOwnerID)
            .orElseThrow { EntityNotFoundException("User not Exists") }

        if (walletDB.isPresent) {
            walletDB.get().walletValue += walletEntity.walletValue
            return EntityToDTOMapper.toWalletDTO(walletRepository.save(walletDB.get()))
        }
        return EntityToDTOMapper.toWalletDTO(walletRepository.save(walletEntity))
    }
}
