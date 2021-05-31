package com.dipay.dto

import com.dipay.constant.UserTypeEnum
import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotEmpty

data class UserDTO(
    @JsonProperty("id")
    val userId: Long,

    @JsonProperty("fullName")
    @field:NotEmpty(message = "Nome completo não pode ser nulo")
    val userFullName: String,

    @JsonProperty("document")
    @field:NotEmpty(message = "Número de documento não pode ser nulo")
    val userDocument: String,

    @JsonProperty("email")
    @field:NotEmpty(message = "Email não pode ser nulo")
    val userEmail: String,

    @JsonProperty("password", access = JsonProperty.Access.WRITE_ONLY)
    @field:NotEmpty(message = "Senha não pode ser nula")
    val userPassword: String,

    @JsonProperty("userType")
    val userType: UserTypeEnum
)
