package com.dipay.util

import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.UnsupportedEncodingException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.security.Security
import java.util.Base64
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException
import javax.crypto.ShortBufferException
import javax.crypto.spec.SecretKeySpec

@Component
object APPUtil {

    private val log = LoggerFactory.getLogger(javaClass)

    @Value("\${secret.key}")
    val secretKey: String? = null

    @JvmStatic
    fun normalizeFields(fieldName: String): String {
        log.info("Normalize field={}", fieldName)

        return fieldName.toLowerCase()
    }

    @JvmStatic
    fun encryptPassword(password: String): String? {
        log.info("Encrypt password init")

        Security.addProvider(BouncyCastleProvider())
        val keyBytes: ByteArray

        try {
            keyBytes = "ASDFGHJKLASDFGHJ".toByteArray(charset("UTF8"))
            val skey = SecretKeySpec(keyBytes, "AES")
            val input = password.toByteArray(charset("UTF8"))

            synchronized(Cipher::class.java) {
                val cipher = Cipher.getInstance("AES/ECB/PKCS7Padding")
                cipher.init(Cipher.ENCRYPT_MODE, skey)

                val cipherText = ByteArray(cipher.getOutputSize(input.size))
                var ctLength = cipher.update(
                    input, 0, input.size,
                    cipherText, 0
                )
                ctLength += cipher.doFinal(cipherText, ctLength)
                return String(
                    Base64.getEncoder().encode(cipherText)
                )
            }
        } catch (uee: UnsupportedEncodingException) {
            log.error(uee.message)
        } catch (ibse: IllegalBlockSizeException) {
            log.error(ibse.message)
        } catch (bpe: BadPaddingException) {
            log.error(bpe.message)
        } catch (ike: InvalidKeyException) {
            log.error(ike.message)
        } catch (nspe: NoSuchPaddingException) {
            log.error(nspe.message)
        } catch (nsae: NoSuchAlgorithmException) {
            log.error(nsae.message)
        } catch (short: ShortBufferException) {
            log.error(short.message)
        }
        return null
    }
}
