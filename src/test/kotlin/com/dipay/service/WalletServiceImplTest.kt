package com.dipay.service

import com.dipay.constant.UserTypeEnum
import com.dipay.entity.TransactionEntity
import com.dipay.entity.UserEntity
import com.dipay.entity.WalletEntity
import com.dipay.exception.TransactionException
import com.dipay.repository.UserRepository
import com.dipay.repository.WalletRepository
import com.dipay.service.impl.WalletServiceImpl
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import java.util.Optional
import javax.persistence.EntityNotFoundException

@SpringBootTest
@AutoConfigureMockMvc
class WalletServiceImplTest {

    @MockBean
    private val userRepository: UserRepository? = null

    @MockBean
    private val walletRepository: WalletRepository? = null

    @Autowired
    private val walletServiceImpl: WalletServiceImpl? = null

    @Test
    fun addValueToWalletFirstTimeWithSuccessTest() {
        given<Optional<WalletEntity>>(walletRepository?.findById(2L))
            .willReturn(Optional.empty())

        given<Optional<UserEntity>>(userRepository?.findById(1L))
            .willReturn(Optional.of(getUser()))

        given<WalletEntity>(walletRepository?.save(anyObject()))
            .willReturn(getWallet(1L))

        val response = walletServiceImpl!!.addValueToWallet(getWallet(1L))
        Assertions.assertNotNull(response)
        Assertions.assertEquals(response.walletValue, 100.00)
    }

    @Test
    fun addValueToWalletWithSuccessTest() {
        given<Optional<WalletEntity>>(walletRepository?.findByWalletOwnerID(1L))
            .willReturn(Optional.of(getWallet(1L)))

        given<Optional<UserEntity>>(userRepository?.findById(1L))
            .willReturn(Optional.of(getUser()))

        given<WalletEntity>(walletRepository?.save(anyObject()))
            .willReturn(getWallet(1L))

        val response = walletServiceImpl!!.addValueToWallet(getWallet(1L))
        Assertions.assertNotNull(response)
        Assertions.assertEquals(response.walletValue, 100.00)
    }

    @Test
    fun addValueToWalletUserNotFoundTest() {
        given<Optional<WalletEntity>>(walletRepository?.findById(1L))
            .willReturn(Optional.of(getWallet(1L)))

        given<Optional<UserEntity>>(userRepository?.findById(1L))
            .willReturn(Optional.empty())

        Assertions.assertThrows(
            EntityNotFoundException::class.java
        ) { walletServiceImpl!!.addValueToWallet(getWallet(1L)) }
    }

    @Test
    fun verifyWalletPayerValueAvailableTest() {
        val wallet = getWallet(1L)
        val transactionEntity = getTransaction()

        given<Optional<WalletEntity>>(walletRepository?.findByWalletOwnerID(1L))
            .willReturn(Optional.of(wallet))

        val response = walletServiceImpl!!.verifyWalletPayerValueAvailable(transactionEntity)

        Assertions.assertNotNull(response)
        Assertions.assertTrue(response.isPresent)
        Assertions.assertEquals(response.get().walletOwnerID, 1L)
    }

    @Test
    fun verifyWalletPayerValueNotAvailableTest() {
        val wallet = getWalletInsufficient(1L)

        val transactionEntity = getTransaction()

        given<Optional<WalletEntity>>(walletRepository?.findByWalletOwnerID(1L))
            .willReturn(Optional.of(wallet))

        Assertions.assertThrows(
            TransactionException::class.java
        ) { walletServiceImpl!!.verifyWalletPayerValueAvailable(transactionEntity) }
    }

    @Test
    fun debitWalletPayerValueSuccessTest() {
        val value = walletServiceImpl!!.debitWalletPayerValue(Optional.of(getWallet(1L)), getTransaction())
        Assertions.assertNotNull(value)
        Assertions.assertEquals(value, 50.00)
    }

    @Test
    fun creditWalletPayeeValue() {
        val value = walletServiceImpl!!.creditWalletPayeeValue(Optional.of(getWallet(2L)), getTransaction())
        Assertions.assertNotNull(value)
        Assertions.assertEquals(value, 150.00)
    }

    @Test
    fun reversalWalletValue() {
        val value = walletServiceImpl!!.reversalWalletValue(Optional.of(getWallet(1L)), getTransaction())
        Assertions.assertNotNull(value)
        Assertions.assertEquals(value, 150.00)
    }

    @Test
    fun debitAndSaveWalletPayerValueTest() {
        walletServiceImpl!!.debitAndSaveWalletPayerValue(Optional.of(getWallet(1L)), getTransaction())
        given<WalletEntity>(walletRepository?.save(anyObject()))
            .willReturn(getWallet(1L))

        verify(walletRepository, times(1))!!.save(anyObject())
    }

    @Test
    fun creditAndSaveWalletPayeeValueTest() {
        given<Optional<WalletEntity>>(walletRepository?.findByWalletOwnerID(2L))
            .willReturn(Optional.of(getWallet(1L)))

        given<WalletEntity>(walletRepository?.save(anyObject()))
            .willReturn(getWallet(1L))

        walletServiceImpl!!.creditAndSaveWalletPayeeValue(getTransaction())

        verify(walletRepository, times(1))!!.save(anyObject())
    }

    @Test
    fun reversalAndSaveWalletValueTest() {
        walletServiceImpl!!.reversalAndSaveWalletValue(Optional.of(getWallet(1L)), getTransaction())
        given<WalletEntity>(walletRepository?.save(anyObject()))
            .willReturn(getWallet(1L))

        verify(walletRepository, times(1))!!.save(anyObject())
    }

    private fun getWallet(ownerID: Long): WalletEntity {
        return WalletEntity(
            1L,
            100.00,
            ownerID
        )
    }

    private fun getUser(): UserEntity {
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

    private fun getWalletInsufficient(ownerID: Long): WalletEntity {
        return WalletEntity(
            1L,
            2.00,
            ownerID
        )
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

    private fun <T> anyObject(): T {
        return Mockito.any()
    }
}
