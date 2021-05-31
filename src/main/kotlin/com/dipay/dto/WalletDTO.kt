package com.dipay.dto

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotNull

data class WalletDTO(
    @JsonProperty("id")
    val walletId: Long,

    @JsonProperty("value")
    @field:NotNull(message = "Valor da carteira não pode ser nulo")
    var walletValue: Double,

    @JsonProperty("owner_id")
    @field:NotNull(message = "ID do usuário dono da carteira não pode ser nulo")
    var walletOwner: Long,
)
