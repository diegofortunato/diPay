package com.dipay.service

import com.dipay.constant.UserTypeEnum
import com.dipay.entity.UserEntity
import com.dipay.entity.WalletEntity
import com.dipay.repository.UserRepository
import com.dipay.service.impl.UserServiceImpl
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.anyString
import org.mockito.BDDMockito.doNothing
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.verify
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import java.util.Optional
import javax.persistence.EntityExistsException
import javax.persistence.EntityNotFoundException

@SpringBootTest
@AutoConfigureMockMvc
class UserServiceImplTest {

    @MockBean
    private val userRepository: UserRepository? = null

    @Autowired
    private val userService: UserServiceImpl? = null

    @Test
    fun createUserWithSuccessTest() {
        given<Optional<UserEntity>>(userRepository?.findByUserEmailOrUserDocument(anyString(), anyString()))
            .willReturn(Optional.empty())

        given<UserEntity>(userRepository?.save(anyObject()))
            .willReturn(getUser())

        val response = userService!!.createUser(getUser())
        Assertions.assertNotNull(response)
        Assertions.assertEquals(response.userId, 1L)
    }

    @Test
    fun createUserWithFailedTest() {
        given<Optional<UserEntity>>(userRepository?.findByUserEmailOrUserDocument(anyString(), anyString()))
            .willReturn(Optional.of(getUser()))

        Assertions.assertThrows(
            EntityExistsException::class.java
        ) { userService!!.createUser(getUser()) }
    }

    @Test
    fun findUserWithSuccessTest() {
        given<Optional<UserEntity>>(userRepository?.findById(1L))
            .willReturn(Optional.of(getUser()))

        val response = userService!!.findUser(1L)
        Assertions.assertNotNull(response)
        Assertions.assertEquals(response.userId, 1L)
    }

    @Test
    fun findUserWithFailedTest() {
        given<Optional<UserEntity>>(userRepository?.findById(1L))
            .willReturn(Optional.empty())

        Assertions.assertThrows(
            EntityNotFoundException::class.java
        ) { userService!!.findUser(1L) }
    }

    @Test
    fun updateUserWithSuccessTest() {
        given<Optional<UserEntity>>(userRepository?.findById(1L))
            .willReturn(Optional.of(getUser()))

        given<Optional<UserEntity>>(userRepository?.findByUserEmailOrUserDocument(anyString(), anyString()))
            .willReturn(Optional.of(getUser()))

        given<UserEntity>(userRepository?.save(anyObject()))
            .willReturn(getUserUpdate())

        val response = userService!!.updateUser(1L, getUser())
        Assertions.assertNotNull(response)
        Assertions.assertEquals(response.userFullName, "Diego Fortunato Candido")
    }

    @Test
    fun updateUserNotFoundTest() {
        given<Optional<UserEntity>>(userRepository?.findById(1L))
            .willReturn(Optional.empty())

        Assertions.assertThrows(
            EntityNotFoundException::class.java
        ) { userService!!.updateUser(1L, getUserUpdate()) }
    }

    @Test
    fun updateUserEmailOrDocumentExistsTest() {
        given<Optional<UserEntity>>(userRepository?.findById(1L))
            .willReturn(Optional.of(getUser()))

        given<Optional<UserEntity>>(userRepository?.findByUserEmailOrUserDocument(anyString(), anyString()))
            .willReturn(Optional.of(getOtherUser()))

        Assertions.assertThrows(
            EntityExistsException::class.java
        ) { userService!!.updateUser(1L, getUserUpdate()) }
    }

    @Test
    fun deleteUserWithSuccessTest() {
        given<Optional<UserEntity>>(userRepository?.findById(1L))
            .willReturn(Optional.of(getUser()))

        doNothing().`when`(userRepository)!!.delete(getUser())

        userService!!.deleteUser(1L)

        verify(userRepository, times(1))!!.delete(getUser())
    }

    @Test
    fun deleteUserWithFailedTest() {
        given<Optional<UserEntity>>(userRepository?.findById(1L))
            .willReturn(Optional.empty())

        Assertions.assertThrows(
            EntityNotFoundException::class.java
        ) { userService!!.deleteUser(1L) }
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
            WalletEntity(
                1L,
                100.00,
                1L
            )
        )
    }

    private fun getUserUpdate(): UserEntity {
        return UserEntity(
            1L,
            "Diego Fortunato Candido",
            "45478963258",
            "diego@email.com",
            "werewrf645fewrewrewrewv45",
            UserTypeEnum.COMMOM,
            arrayListOf(),
            arrayListOf(),
            null
        )
    }

    private fun getOtherUser(): UserEntity {
        return UserEntity(
            2L,
            "Leslie Possidonio",
            "532532534",
            "diego@email.com",
            "werewrf645fewrewrewrewv45",
            UserTypeEnum.COMMOM,
            arrayListOf(),
            arrayListOf(),
            null
        )
    }

    private fun <T> anyObject(): T {
        return Mockito.any()
    }
}
