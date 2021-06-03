package com.dipay.controller

import com.dipay.constant.APIConstant
import com.dipay.dto.WalletDTO
import com.dipay.service.WalletService
import com.dipay.service.impl.WalletServiceImpl
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
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
class WalletControllerTest {

    @Autowired
    private val mvc: MockMvc? = null

    @MockBean
    private val service: WalletServiceImpl? = null

    @Test
    fun addValueToWalletTest() {
        val request = getJson("request-wallet.json")

        `when`(service?.addValueToWallet(anyObject())).thenReturn(getWallet())

        mvc!!.perform(
            MockMvcRequestBuilders
                .post(APIConstant.BASE_API + APIConstant.POST_WALLET_API)
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(1L))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.value").value(100.00))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.owner_id").value(1L))
    }

    private fun getWallet(): WalletDTO {
        return WalletDTO(
            1L,
            100.00,
            1L
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
