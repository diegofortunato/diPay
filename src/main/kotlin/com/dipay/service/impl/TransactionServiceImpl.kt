package com.dipay.service.impl

import com.dipay.config.RetrofitConfig
import com.dipay.constant.APIConstant
import com.dipay.constant.UserTypeEnum
import com.dipay.dto.TransactionDTO
import com.dipay.entity.TransactionEntity
import com.dipay.entity.WalletEntity
import com.dipay.exception.TransactionException
import com.dipay.repository.TransactionRepository
import com.dipay.repository.UserRepository
import com.dipay.repository.WalletRepository
import com.dipay.service.TransactionService
import com.dipay.util.mapper.EntityToDTOMapper
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.Optional
import javax.persistence.EntityNotFoundException

@Service
class TransactionServiceImpl(
    private val transactionRepository: TransactionRepository,
    private val userRepository: UserRepository,
    private val walletRepository: WalletRepository
) : TransactionService {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun performTransaction(value: Double, userPayer: Long, userPayee: Long): TransactionDTO {
        log.info("Perform transaction service. value={}", value)

        val transactionEntity = verifyUserConditions(value, userPayer, userPayee)

        val walletPayer = verifyWalletPayerValueAvailable(transactionEntity)

        verifyUserAuth()

        walletPayer.get().walletValue = debitWalletPayerValue(walletPayer, transactionEntity)
        walletRepository.save(walletPayer.get())

        try {
            val walletPayee = walletRepository.findByWalletOwnerID(transactionEntity.userPayee!!.userId)
            walletPayee.get().walletValue = creditWalletPayeeValue(walletPayee, transactionEntity)
            walletRepository.save(walletPayee.get())
        } catch (e: Exception) {
            log.error(
                "An error occurred while carrying out a transaction, the amount is being refunded. value={}",
                transactionEntity.transactionValue
            )
            walletPayer.get().walletValue = reversalWalletValue(walletPayer, transactionEntity)
            walletRepository.save(walletPayer.get())
        }

        notificationUser()

        return EntityToDTOMapper.toTransactionDTO(transactionRepository.save(transactionEntity))
    }

    fun verifyUserConditions(value: Double, userPayer: Long, userPayee: Long): TransactionEntity {
        val userPayerDB = userRepository.findById(userPayer)
            .orElseThrow { EntityNotFoundException("User Payer not Exist") }

        val userPayeeDB = userRepository.findById(userPayee)
            .orElseThrow { EntityNotFoundException("User Payee not Exist") }

        if (userPayerDB.userType == UserTypeEnum.SHOPKEEPER)
            throw TransactionException(APIConstant.ERROR_TRANSACTION_NO_SHOPKEEPER)

        return TransactionEntity(null, value, userPayerDB, userPayeeDB)
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

    fun creditWalletPayeeValue(walletPayee: Optional<WalletEntity>, transactionEntity: TransactionEntity): Double {
        return walletPayee.get().walletValue + transactionEntity.transactionValue
    }

    fun reversalWalletValue(walletPayer: Optional<WalletEntity>, transactionEntity: TransactionEntity): Double {
        return walletPayer.get().walletValue + transactionEntity.transactionValue
    }

    fun verifyUserAuth() {
        val call = RetrofitConfig().authorizationService().authUser()
        val message = call.execute().body()!!.message
        if (message != APIConstant.AUTH_SUCCESS_MESSAGE)
            throw TransactionException(APIConstant.USER_TRANSACTION_NOT_AUTHORIZED)
    }

    fun notificationUser() {
        val call = RetrofitConfig().notificationService().notificationUser()
        val message = call.execute().body()!!.message
        if (message != APIConstant.NOTIFICATION_SUCCESS_MESSAGE)
            throw TransactionException(APIConstant.USER_TRANSACTION_NOT_NOTIFICATION)
    }
}
