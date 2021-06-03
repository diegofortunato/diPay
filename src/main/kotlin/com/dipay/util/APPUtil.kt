package com.dipay.util

import org.mindrot.jbcrypt.BCrypt
import org.slf4j.LoggerFactory

object APPUtil {

    private val log = LoggerFactory.getLogger(javaClass)

    @JvmStatic
    fun normalizeFields(fieldName: String): String {
        log.info("Normalize field={}", fieldName)

        return fieldName.toLowerCase()
    }

    @JvmStatic
    fun encryptPassword(password: String): String? {
        log.info("Init encrypt Password")

        return BCrypt.hashpw(password, BCrypt.gensalt())
    }
}
