package com.dipay.service

import com.dipay.constant.UserTypeEnum
import com.dipay.entity.UserEntity
import com.dipay.entity.WalletEntity
import com.dipay.repository.UserRepository
import com.dipay.repository.WalletRepository
import com.dipay.service.impl.WalletServiceImpl
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito
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
        given<Optional<WalletEntity>>(walletRepository?.findById(1L))
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

    private fun <T> anyObject(): T {
        return Mockito.any()
    }
}
