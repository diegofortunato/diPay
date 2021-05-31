package com.dipay.controller

import com.dipay.constant.APIConstant
import com.dipay.controller.request.Request
import com.dipay.controller.response.Response
import com.dipay.dto.UserDTO
import com.dipay.service.UserService
import com.dipay.util.mapper.DTOTOEntityMapper
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import java.net.URLEncoder
import javax.validation.Valid

@RestController
@RequestMapping(value = [APIConstant.BASE_API])
class UserController(private val userService: UserService) {

    private val log = LoggerFactory.getLogger(javaClass)

    @PostMapping(APIConstant.POST_USER_API)
    fun createUser(@Valid @RequestBody userRequest: Request<UserDTO>): ResponseEntity<Response<UserDTO>> {
        log.info("POST ${APIConstant.POST_USER_API} for user {}", userRequest.request.userFullName)

        val user = userService.createUser(DTOTOEntityMapper.toUserEntity(userRequest.request))
        return ResponseEntity
            .created(URI.create(URLEncoder.encode(APIConstant.BASE_API + APIConstant.POST_USER_API, "UTF-8")))
            .body(Response(data = user))
    }

    @GetMapping(APIConstant.GET_USER_API)
    fun findUser(@PathVariable("id") userID: Long): ResponseEntity<Response<UserDTO>> {
        log.info("GET ${APIConstant.GET_USER_API} for ID {}", userID)

        val user = userService.findUser(userID)
        return ResponseEntity.ok(Response(data = user))
    }

    @PatchMapping(APIConstant.PATCH_USER_API)
    fun updateeUser(@PathVariable("id") userID: Long, @Valid @RequestBody userRequest: Request<UserDTO>):
        ResponseEntity<Response<UserDTO>> {
        log.info("PATCH ${APIConstant.PATCH_USER_API} for ID {}", userRequest.request.userId)

        val user = userService.updateUser(userID, DTOTOEntityMapper.toUserEntity(userRequest.request))
        return ResponseEntity.ok(Response(data = user))
    }

    @DeleteMapping(APIConstant.DELETE_USER_API)
    fun deleteUser(@PathVariable("id") userID: Long): ResponseEntity<Response<UserDTO>> {
        log.info("DELETE ${APIConstant.DELETE_USER_API} for ID {}", userID)

        userService.deleteUser(userID)
        return ResponseEntity.noContent().build()
    }
}
