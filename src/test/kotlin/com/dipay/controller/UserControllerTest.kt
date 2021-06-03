package com.dipay.controller

import com.dipay.constant.APIConstant
import com.dipay.constant.UserTypeEnum
import com.dipay.dto.UserDTO
import com.dipay.entity.UserEntity
import com.dipay.entity.WalletEntity
import com.dipay.service.impl.UserServiceImpl
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.doNothing
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.io.File
import java.nio.file.Files

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private val mvc: MockMvc? = null

    @MockBean
    private val service: UserServiceImpl? = null

    @Test
    fun createUserTest() {
        val request = getJson("request-user.json")

        `when`(service?.createUser(anyObject())).thenReturn(getUser())

        mvc!!.perform(
            MockMvcRequestBuilders
                .post(APIConstant.BASE_API + APIConstant.POST_USER_API)
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(1L))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.fullName").value("Diego Fortunato"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.document").value("65464646464"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.email").value("diego@gmail.com"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.userType").value("COMMOM"))
    }

    @Test
    fun findUserTest() {
        `when`(service?.findUser(1L)).thenReturn(getUser())

        mvc!!.perform(
            MockMvcRequestBuilders
                .get(APIConstant.BASE_API + APIConstant.GET_USER_API, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(1L))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.fullName").value("Diego Fortunato"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.document").value("65464646464"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.email").value("diego@gmail.com"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.userType").value("COMMOM"))
    }

    @Test
    fun updateeUserTest() {
        val request = getJson("request-user.json")

        `when`(service?.updateUser(1L, getUserEntity())).thenReturn(getUser())

        mvc!!.perform(
            MockMvcRequestBuilders
                .patch(APIConstant.BASE_API + APIConstant.PATCH_USER_API, 1)
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun deleteUserTest() {
        doNothing().`when`(service)!!.deleteUser(1L)

        mvc!!.perform(
            MockMvcRequestBuilders
                .delete(APIConstant.BASE_API + APIConstant.DELETE_USER_API, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isNoContent)
    }

    private fun getUserEntity(): UserEntity {
        return UserEntity(
            1L,
            "Diego Fortunato",
            "65464646464",
            "diego@gmail.com",
            "123456",
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

    private fun getUser(): UserDTO {
        return UserDTO(
            1L,
            "Diego Fortunato",
            "65464646464",
            "diego@gmail.com",
            "123456",
            UserTypeEnum.COMMOM
        )
    }

    fun getJson(fileName: String): String {
        val classLoader = javaClass.classLoader
        val file = File(classLoader.getResource(fileName).file)
        return String(Files.readAllBytes(file.toPath()))
    }

    private fun <T> anyObject(): T {
        return Mockito.any()
    }
}
