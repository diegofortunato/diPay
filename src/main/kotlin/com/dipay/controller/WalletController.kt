package com.dipay.controller

import com.dipay.constant.APIConstant
import com.dipay.controller.request.Request
import com.dipay.controller.response.Response
import com.dipay.dto.WalletDTO
import com.dipay.service.WalletService
import com.dipay.util.mapper.DTOTOEntityMapper
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import java.net.URLEncoder
import javax.validation.Valid

@RestController
@RequestMapping(value = [APIConstant.BASE_API])
class WalletController(private val walletService: WalletService) {

    private val log = LoggerFactory.getLogger(javaClass)

    @PostMapping(APIConstant.POST_WALLET_API)
    fun addValueToWallet(@Valid @RequestBody walletRequest: Request<WalletDTO>):
        ResponseEntity<Response<WalletDTO>> {
        log.info("POST ${APIConstant.POST_WALLET_API} for user {}", walletRequest.request.walletOwner)

        val wallet = walletService
            .addValueToWallet(DTOTOEntityMapper.toWalletEntity(walletRequest.request))
        return ResponseEntity
            .created(URI.create(URLEncoder.encode(APIConstant.BASE_API + APIConstant.POST_WALLET_API, "UTF-8")))
            .body(Response(data = wallet))
    }
}
