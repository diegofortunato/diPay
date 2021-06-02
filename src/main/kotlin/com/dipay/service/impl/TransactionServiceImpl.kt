package com.dipay.service.impl

import com.dipay.dto.TransactionDTO
import com.dipay.repository.TransactionRepository
import com.dipay.service.TransactionService
import com.dipay.util.extension.EntityToDTOExtension.toDTO
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class TransactionServiceImpl(
    private val transactionRepository: TransactionRepository,
    private val walletService: WalletServiceImpl,
    private val userService: UserServiceImpl,
    private val authorizationService: AuthorizationServiceImpl,
    private val notificationService: NotificationServiceImpl
) : TransactionService {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun performTransaction(value: Double, userPayer: Long, userPayee: Long): TransactionDTO {
        log.info("Perform transaction service. value={}", value)

        val transactionEntity = userService.verifyUserConditions(value, userPayer, userPayee)

        val walletPayer = walletService.verifyWalletPayerValueAvailable(transactionEntity)

        authorizationService.verifyUserAuth()

        try {
            walletService.debitAndSaveWalletPayerValue(walletPayer, transactionEntity)
            walletService.creditAndSaveWalletPayeeValue(transactionEntity)
        } catch (e: Exception) {
            log.error(
                "An error occurred while carrying out a transaction, the amount is being refunded. value={}",
                transactionEntity.transactionValue
            )
            walletService.reversalAndSaveWalletValue(walletPayer, transactionEntity)
        }

        notificationService.notificationUser()

        return transactionRepository.save(transactionEntity).toDTO()
    }
}
