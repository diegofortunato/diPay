package com.dipay.service

import com.dipay.constant.UserTypeEnum
import com.dipay.entity.TransactionEntity
import com.dipay.entity.UserEntity
import com.dipay.entity.WalletEntity
import com.dipay.exception.TransactionException
import com.dipay.repository.TransactionRepository
import com.dipay.repository.UserRepository
import com.dipay.repository.WalletRepository
import com.dipay.service.impl.TransactionServiceImpl
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import java.util.Optional
import javax.persistence.EntityNotFoundException

@SpringBootTest
@AutoConfigureMockMvc
class TransactionServiceImplTest {

    @MockBean
    private val transactionRepository: TransactionRepository? = null

    @MockBean
    private val userRepository: UserRepository? = null

    @MockBean
    private val walletRepository: WalletRepository? = null

    @Autowired
    private val transactioService: TransactionServiceImpl ? = null

    @Test
    fun verifyUserConditionsSucessTest() {
        val userPayer = getUserPayer()
        val userPayee = getUserPayee()

        given<Optional<UserEntity>>(userRepository?.findById(1L))
            .willReturn(Optional.of(userPayer))

        given<Optional<UserEntity>>(userRepository?.findById(2L))
            .willReturn(Optional.of(userPayee))

        val response = transactioService!!.verifyUserConditions(100.00, 1L, 2L)

        Assertions.assertNotNull(response)
        Assertions.assertEquals(response.userPayer!!.userId, 1L)
        Assertions.assertEquals(response.userPayee!!.userId, 2L)
    }

    @Test
    fun verifyUserConditionsUserPayerNotFoundTest() {
        given<Optional<UserEntity>>(userRepository?.findById(9L))
            .willReturn(Optional.empty())
        Assertions.assertThrows(
            EntityNotFoundException::class.java
        ) { transactioService!!.verifyUserConditions(100.00, 9L, 2L) }
    }

    @Test
    fun verifyUserConditionsUserPayeeNotFoundTest() {
        val userPayer = getUserPayer()

        given<Optional<UserEntity>>(userRepository?.findById(1L))
            .willReturn(Optional.of(userPayer))
        given<Optional<UserEntity>>(userRepository?.findById(10L))
            .willReturn(Optional.empty())
        Assertions.assertThrows(
            EntityNotFoundException::class.java
        ) { transactioService!!.verifyUserConditions(100.00, 1L, 10L) }
    }

    @Test
    fun verifyUserConditionsErrorTest() {
        val userPayer = getUserShopkeeper()
        val userPayee = getUserPayee()

        given<Optional<UserEntity>>(userRepository?.findById(3L))
            .willReturn(Optional.of(userPayer))

        given<Optional<UserEntity>>(userRepository?.findById(2L))
            .willReturn(Optional.of(userPayee))

        Assertions.assertThrows(
            TransactionException::class.java
        ) { transactioService!!.verifyUserConditions(100.00, 3L, 2L) }
    }

    @Test
    fun verifyWalletPayerValueAvailableTest() {
        val wallet = getWallet(1L)
        val transactionEntity = getTransaction()

        given<Optional<WalletEntity>>(walletRepository?.findByWalletOwnerID(1L))
            .willReturn(Optional.of(wallet))

        val response = transactioService!!.verifyWalletPayerValueAvailable(transactionEntity)

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
        ) { transactioService!!.verifyWalletPayerValueAvailable(transactionEntity) }
    }

    @Test
    fun debitWalletPayerValueSuccessTest() {
        val value = transactioService!!.debitWalletPayerValue(Optional.of(getWallet(1L)), getTransaction())
        Assertions.assertNotNull(value)
        Assertions.assertEquals(value, 50.00)
    }

    @Test
    fun creditWalletPayeeValue() {
        val value = transactioService!!.creditWalletPayeeValue(Optional.of(getWallet(2L)), getTransaction())
        Assertions.assertNotNull(value)
        Assertions.assertEquals(value, 150.00)
    }

    @Test
    fun reversalWalletValue() {
        val value = transactioService!!.reversalWalletValue(Optional.of(getWallet(1L)), getTransaction())
        Assertions.assertNotNull(value)
        Assertions.assertEquals(value, 150.00)
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

    private fun getUserShopkeeper(): UserEntity {
        return UserEntity(
            3L,
            "Ana Barbosa",
            "3243253454",
            "ana@email.com",
            "5354534543543543543",
            UserTypeEnum.SHOPKEEPER,
            arrayListOf(),
            arrayListOf(),
            getWallet(3L)
        )
    }

    private fun getWallet(ownerID: Long): WalletEntity {
        return WalletEntity(
            1L,
            100.00,
            ownerID
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
}
