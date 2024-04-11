package com.rodrigo.authenticationjwt.repository

import com.rodrigo.authenticationjwt.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, String> {
    fun findUserByUsername(username: String): User?
}