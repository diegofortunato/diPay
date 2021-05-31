package com.dipay.repository

import com.dipay.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface UserRepository : JpaRepository<UserEntity, Long> {

    fun findByUserEmailOrUserDocument(email: String, document: String): Optional<UserEntity>
}
