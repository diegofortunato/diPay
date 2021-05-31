package com.dipay.dto

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotNull

data class TransactionDTO(
    @JsonProperty("id")
    val transactionId: Long?,

    @JsonProperty("value")
    @field:NotNull(message = "Valor da transação não pode ser nulo")
    var transactionValue: Double,

    @JsonProperty("payer")
    @field:NotNull(message = "ID de usuário pagador não pode ser nulo")
    var userPayer: Long,

    @JsonProperty("payee")
    @field:NotNull(message = "ID de usuário recebedor não pode ser nulo")
    var userPayee: Long,
)
