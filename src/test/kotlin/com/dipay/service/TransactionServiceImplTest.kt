package com.dipay.service

import com.dipay.constant.UserTypeEnum
import com.dipay.entity.TransactionEntity
import com.dipay.entity.UserEntity
import com.dipay.entity.WalletEntity
import com.dipay.exception.TransactionException
import com.dipay.repository.TransactionRepository
import com.dipay.service.impl.AuthorizationServiceImpl
import com.dipay.service.impl.NotificationServiceImpl
import com.dipay.service.impl.TransactionServiceImpl
import com.dipay.service.impl.UserServiceImpl
import com.dipay.service.impl.WalletServiceImpl
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.`when`
import org.mockito.BDDMockito.doNothing
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import java.util.Optional

@SpringBootTest
@AutoConfigureMockMvc
class TransactionServiceImplTest {

    @MockBean
    private val transactionRepository: TransactionRepository? = null

    @Mock
    private val userService: UserServiceImpl? = null

    @Mock
    private val walletService: WalletServiceImpl? = null

    @Mock
    private val authService: AuthorizationServiceImpl? = null

    @Mock
    private val notificationService: NotificationServiceImpl? = null

    @Test
    fun performTransactionTest() {
        `when`(userService!!.verifyUserConditions(50.0, 1, 2)).thenReturn(getTransaction())

        `when`(walletService!!.verifyWalletPayerValueAvailable(getTransaction())).thenReturn(Optional.of(getWallet(1L)))

        doNothing().`when`(authService)!!.verifyUserAuth()

        doNothing().`when`(notificationService)!!.notificationUser()

        `when`(transactionRepository?.save(anyObject())).thenReturn(getTransaction())

        val transactioService = TransactionServiceImpl(transactionRepository!!, walletService, userService, authService!!, notificationService!!)

        val response = transactioService.performTransaction(50.0, 1, 2)

        Assertions.assertNotNull(response)
        Assertions.assertEquals(response.transactionId, 1L)
        Assertions.assertEquals(response.transactionValue, 50.0)
        Assertions.assertEquals(response.userPayer, 1)
        Assertions.assertEquals(response.userPayee, 2)
    }

    @Test
    fun performTransactionErrorTest() {
        `when`(userService!!.verifyUserConditions(50.0, 1, 2)).thenReturn(getTransaction())

        `when`(walletService!!.verifyWalletPayerValueAvailable(getTransaction())).thenReturn(Optional.of(getWallet(1L)))

        doNothing().`when`(authService)!!.verifyUserAuth()

        `when`(walletService.debitAndSaveWalletPayerValue(Optional.of(getWallet(1L)), getTransaction()))
            .thenThrow(RuntimeException::class.java)

        val transactioService = TransactionServiceImpl(transactionRepository!!, walletService, userService, authService!!, notificationService!!)

        Assertions.assertThrows(
            Exception::class.java
        ) { transactioService.performTransaction(50.0, 1L, 2L) }

        verify(walletService, times(1))!!.reversalAndSaveWalletValue(Optional.of(getWallet(1L)), getTransaction())
    }

    private fun getTransaction(): TransactionEntity {
        return TransactionEntity(
            1L,
            50.00,
            getUserPayer(),
            getUserPayee()
        )
    }

    private fun getUserPayer(): UserEntity {
        return UserEntity(
            1L,
            "Diego Fortunato",
            "45478963258",
            "diego@email.com",
            "werewrf645fewrewrewrewv45",
            UserTypeEnum.COMMOM,
            arrayListOf(),
            arrayListOf(),
            getWallet(1L)
        )
    }

    private fun getUserPayee(): UserEntity {
        return UserEntity(
            2L,
            "Marcos Andrade",
            "45478234258",
            "marcos@email.com",
            "4534egfg443r",
            UserTypeEnum.SHOPKEEPER,
            arrayListOf(),
            arrayListOf(),
            getWallet(2L)
        )
    }

    private fun getWallet(ownerID: Long): WalletEntity {
        return WalletEntity(
            1L,
            100.00,
            ownerID
        )
    }

    private fun <T> anyObject(): T {
        return Mockito.any()
    }
}
