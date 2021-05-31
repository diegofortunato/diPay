package com.dipay.controller.advice

import com.dipay.constant.APIConstant
import com.dipay.controller.response.ErrorResponse
import com.dipay.controller.response.Response
import com.dipay.exception.TransactionException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import java.time.Instant
import javax.persistence.EntityExistsException
import javax.persistence.EntityNotFoundException

@ControllerAdvice
class RestExceptionHandler {

    private val log = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(value = [(EntityExistsException::class)])
    fun handleEntityExistsException(ex: EntityExistsException, request: WebRequest):
        ResponseEntity<Response<ErrorResponse>> {
        log.error("Error in handleEntityExistsException: {}", ex.message)

        val errorResponse = ErrorResponse(
            Instant.now().toString(),
            HttpStatus.BAD_REQUEST.value(),
            APIConstant.ERROR_400,
            APIConstant.DETAILS_ERROR_400
        )
        val response = Response(data = errorResponse)
        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(value = [(TransactionException::class)])
    fun handleTransactionException(ex: TransactionException, request: WebRequest):
        ResponseEntity<Response<ErrorResponse>> {
        log.error("Error in handleTransactionException: {}", ex.message)

        val errorResponse = ErrorResponse(
            Instant.now().toString(),
            HttpStatus.BAD_REQUEST.value(),
            APIConstant.ERROR_TRANSACTION_400,
            ex.message!!
        )
        val response = Response(data = errorResponse)
        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(value = [(EntityNotFoundException::class)])
    fun handleEntityNotFoundException(ex: EntityNotFoundException, request: WebRequest):
        ResponseEntity<Response<ErrorResponse>> {
        log.error("Error in handleEntityNotFoundException: {}", ex.message)

        val errorResponse = ErrorResponse(
            Instant.now().toString(),
            HttpStatus.NOT_FOUND.value(),
            APIConstant.ERROR_404,
            APIConstant.DETAILS_ERROR_404
        )
        val response = Response(data = errorResponse)
        return ResponseEntity(response, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(value = [(HttpMessageNotReadableException::class)])
    fun handleMethodArgumentNotValidException(ex: HttpMessageNotReadableException, request: WebRequest):
        ResponseEntity<Response<ErrorResponse>> {
        log.error("Error in handleMethodArgumentNotValidException: {}", ex.message)

        val errorResponse = ErrorResponse(
            Instant.now().toString(),
            HttpStatus.PRECONDITION_FAILED.value(),
            APIConstant.ERROR_412,
            ex.message!!
        )
        val response = Response(data = errorResponse)
        return ResponseEntity(response, HttpStatus.PRECONDITION_FAILED)
    }

    @ExceptionHandler(value = [(Exception::class)])
    fun handleException(ex: Exception, request: WebRequest):
        ResponseEntity<Response<ErrorResponse>> {
        log.error("Error in handleException: {}", ex.message)

        val errorResponse = ErrorResponse(
            Instant.now().toString(),
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            APIConstant.ERROR_500,
            APIConstant.DETAILS_ERROR_500
        )
        val response = Response(data = errorResponse)
        return ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    fun getErrorsValidation(ex: MethodArgumentNotValidException): ArrayList<String> {
        val result = ex.bindingResult
        val fieldErrors = result.fieldErrors
        val errorsList = ArrayList<String>()

        fieldErrors.forEach {
            errorsList.add(it.defaultMessage!!)
        }
        return errorsList
    }
}