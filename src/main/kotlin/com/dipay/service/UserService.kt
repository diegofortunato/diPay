package com.dipay.service

import com.dipay.dto.UserDTO
import com.dipay.entity.UserEntity

interface UserService {
    fun createUser(userEntity: UserEntity): UserDTO
    fun findUser(userID: Long): UserDTO
    fun updateUser(userID: Long, userEntity: UserEntity): UserDTO
    fun deleteUser(userID: Long)
}
