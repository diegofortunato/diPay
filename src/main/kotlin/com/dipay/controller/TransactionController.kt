package com.dipay.controller

import com.dipay.constant.APIConstant
import com.dipay.controller.request.Request
import com.dipay.controller.response.Response
import com.dipay.dto.TransactionDTO
import com.dipay.service.TransactionService
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
class TransactionController(private val transactionService: TransactionService) {

    private val log = LoggerFactory.getLogger(javaClass)

    @PostMapping(APIConstant.POST_TRANSACTION_API)
    fun performTransaction(@Valid @RequestBody transactionRequest: Request<TransactionDTO>):
        ResponseEntity<Response<TransactionDTO>> {
        log.info("POST ${APIConstant.POST_TRANSACTION_API} for user {}", transactionRequest.request.userPayer)

        val value = transactionRequest.request.transactionValue
        val userPayer = transactionRequest.request.userPayer
        val userPayee = transactionRequest.request.userPayee

        val transaction = transactionService.performTransaction(value, userPayer, userPayee)
        return ResponseEntity
            .created(URI.create(URLEncoder.encode(APIConstant.BASE_API + APIConstant.POST_TRANSACTION_API, "UTF-8")))
            .body(Response(data = transaction))
    }
}
