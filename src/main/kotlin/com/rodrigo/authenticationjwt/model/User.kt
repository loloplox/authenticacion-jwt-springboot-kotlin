package com.rodrigo.authenticationjwt.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.util.UUID

@Entity
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String = UUID.randomUUID().toString(),
    val username: String,
    val password: String,
)

data class UserRequest(
    val username: String,
    val password: String,
)

data class AuthenticationUser(
    val username: String? = null,
    val token: String,
)