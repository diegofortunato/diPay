package com.dipay.constant

class APIConstant {

    companion object {
        const val BASE_API = ""
        const val POST_USER_API = "/user"
        const val GET_USER_API = "/user/{id}"
        const val PATCH_USER_API = "/user/{id}"
        const val DELETE_USER_API = "/user/{id}"

        const val POST_WALLET_API = "/wallet"

        const val POST_TRANSACTION_API = "/transaction"

        const val BASE_AUTH_INTEGRATION = "https://run.mocky.io/v3/"
        const val BASE_NOTIFICATION_INTEGRATION = "http://o4d9z.mocklab.io/"

        const val AUTH_INTEGRATION = "8fafdd68-a090-496f-8c9a-3442cf30dae6"
        const val NOTIFICATION_INTEGRATION = "notify"

        const val ERROR_TRANSACTION_400 = "Ocorreu um erro durante a transação."
        const val ERROR_TRANSACTION_NO_MONEY = "Você não possui saldo sulficiente para completar essa transação."
        const val ERROR_TRANSACTION_NO_SHOPKEEPER = "Lojistas não possuem permissão para enviar dinheiro."
        const val USER_TRANSACTION_NOT_AUTHORIZED = "Usuário não esta autorizado a realizar esta transação."
        const val USER_TRANSACTION_NOT_NOTIFICATION = "Usuário não conseguiu ser notificado sobre essa transação."
        const val AUTH_SUCCESS_MESSAGE = "Autorizado"
        const val NOTIFICATION_SUCCESS_MESSAGE = "Success"

        const val ERROR_400 = "Usuário já existe no sistema."
        const val DETAILS_ERROR_400 = "O usuário já existe no sistema, " +
            "verifique o email e o número do documento."

        const val ERROR_404 = "Usuário não existe no sistema."
        const val DETAILS_ERROR_404 = "O usuário não existe no sistema, " +
            "verifique o seu ID."

        const val ERROR_412 = "Existem campos com Inconsistências."

        const val ERROR_500 = "Erro interno do servidor."
        const val DETAILS_ERROR_500 = "Ocoreu um erro interno no servidor, " +
            "entre em contato com o administrador do sistema."
    }
}
