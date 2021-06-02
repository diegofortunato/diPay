package com.dipay.service.impl

import com.dipay.constant.APIConstant
import com.dipay.dto.WalletDTO
import com.dipay.entity.TransactionEntity
import com.dipay.entity.WalletEntity
import com.dipay.exception.TransactionException
import com.dipay.repository.UserRepository
import com.dipay.repository.WalletRepository
import com.dipay.service.WalletService
import com.dipay.util.extension.EntityToDTOExtension.toDTO
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.Optional
import javax.persistence.EntityNotFoundException

@Service
class WalletServiceImpl(
    private val walletRepository: WalletRepository,
    private val userRepository: UserRepository
) : WalletService {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun addValueToWallet(walletEntity: WalletEntity): WalletDTO {
        log.info("Add value to wallet service. value={}", walletEntity.walletValue)

        val walletDB = walletRepository.findByWalletOwnerID(walletEntity.walletOwnerID)

        userRepository.findById(walletEntity.walletOwnerID)
            .orElseThrow { EntityNotFoundException("User not Exists") }

        if (walletDB.isPresent) {
            walletDB.get().walletValue += walletEntity.walletValue
            return walletRepository.save(walletDB.get()).toDTO()
        }
        return walletRepository.save(walletEntity).toDTO()
    }

    fun verifyWalletPayerValueAvailable(transactionEntity: TransactionEntity): Optional<WalletEntity> {
        val walletPayer = walletRepository.findByWalletOwnerID(transactionEntity.userPayer!!.userId)
        if (walletPayer.isEmpty || walletPayer.get().walletValue < transactionEntity.transactionValue)
            throw TransactionException(APIConstant.ERROR_TRANSACTION_NO_MONEY)
        return walletPayer
    }

    fun debitWalletPayerValue(walletPayer: Optional<WalletEntity>, transactionEntity: TransactionEntity): Double {
        return walletPayer.get().walletValue - transactionEntity.transactionValue
    }

    fun debitAndSaveWalletPayerValue(walletPayer: Optional<WalletEntity>, transactionEntity: TransactionEntity) {
        walletPayer.get().walletValue = debitWalletPayerValue(walletPayer, transactionEntity)
        walletRepository.save(walletPayer.get())
    }

    fun creditWalletPayeeValue(walletPayee: Optional<WalletEntity>, transactionEntity: TransactionEntity): Double {
        return walletPayee.get().walletValue + transactionEntity.transactionValue
    }

    fun creditAndSaveWalletPayeeValue(transactionEntity: TransactionEntity) {
        val walletPayee = walletRepository.findByWalletOwnerID(transactionEntity.userPayee!!.userId)
        walletPayee.get().walletValue = creditWalletPayeeValue(walletPayee, transactionEntity)
        walletRepository.save(walletPayee.get())
    }

    fun reversalWalletValue(walletPayer: Optional<WalletEntity>, transactionEntity: TransactionEntity): Double {
        return walletPayer.get().walletValue + transactionEntity.transactionValue
    }

    fun reversalAndSaveWalletValue(walletPayer: Optional<WalletEntity>, transactionEntity: TransactionEntity) {
        walletPayer.get().walletValue = reversalWalletValue(walletPayer, transactionEntity)
        walletRepository.save(walletPayer.get())
    }
}
