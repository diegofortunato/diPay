package com.dipay.util

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc

@AutoConfigureMockMvc
class APPUtilTest {

    @Test
    fun normalizeFieldsTest() {
        val result = APPUtil.normalizeFields("DIEGO@GMAIL.COM")
        Assertions.assertEquals(result, "diego@gmail.com")
    }

    @Test
    fun encryptPasswordTest() {
        val result = APPUtil.encryptPassword("123456")
        Assertions.assertNotEquals(result, "123456")
    }
}
