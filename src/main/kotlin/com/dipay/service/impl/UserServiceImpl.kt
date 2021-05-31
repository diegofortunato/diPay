package com.dipay.service.impl

import com.dipay.dto.UserDTO
import com.dipay.entity.UserEntity
import com.dipay.repository.UserRepository
import com.dipay.service.UserService
import com.dipay.util.APPUtil
import com.dipay.util.mapper.EntityToDTOMapper
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.Optional
import javax.persistence.EntityExistsException
import javax.persistence.EntityNotFoundException

@Service
class UserServiceImpl(private val userRepository: UserRepository) : UserService {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun createUser(userEntity: UserEntity): UserDTO {
        log.info("Create User service. userName={}", userEntity.userFullName)

        userEntity.userEmail = APPUtil.normalizeFields(userEntity.userEmail)
        userEntity.userDocument = APPUtil.normalizeFields(userEntity.userDocument)

        val user = isExistUser(userEntity.userEmail, userEntity.userDocument)
        if (user.isPresent) throw EntityExistsException("User exists")

        userEntity.userPassword = APPUtil.encryptPassword(userEntity.userPassword)!!

        return EntityToDTOMapper.toUserDTO(userRepository.save(userEntity))
    }

    override fun findUser(userID: Long): UserDTO {
        log.info("Find User service. userID={}", userID)

        val userDB = userRepository.findById(userID)
            .orElseThrow { EntityNotFoundException("User not Exists") }

        return EntityToDTOMapper.toUserDTO(userDB)
    }

    override fun updateUser(userID: Long, userEntity: UserEntity): UserDTO {
        log.info("Update User service. userName={}", userEntity.userFullName)

        val userDB = userRepository.findById(userID)
            .orElseThrow { EntityNotFoundException("User not Exists") }

        val user = isExistUser(userDB.userEmail, userDB.userDocument)

        if (user.isPresent && user.get().userId == userDB.userId) {
            updateFieldsUser(userDB, userEntity)
        } else {
            throw EntityExistsException("Email or document already exists in the system")
        }

        return EntityToDTOMapper.toUserDTO(userRepository.save(userDB))
    }

    override fun deleteUser(userID: Long) {
        log.info("Delete User service. userID={}", userID)

        val userDB = userRepository.findById(userID)
            .orElseThrow { EntityNotFoundException("User not Exists") }

        userRepository.delete(userDB)
    }

    private fun isExistUser(email: String, document: String): Optional<UserEntity> {
        return userRepository.findByUserEmailOrUserDocument(email, document)
    }

    private fun updateFieldsUser(userDB: UserEntity, userEntity: UserEntity) {
        userDB.userFullName = userEntity.userFullName
        userDB.userDocument = APPUtil.normalizeFields(userEntity.userDocument)
        userDB.userEmail = APPUtil.normalizeFields(userEntity.userEmail)
        userDB.userPassword = APPUtil.encryptPassword(userEntity.userPassword)!!
        userDB.userType = userEntity.userType
    }
}
