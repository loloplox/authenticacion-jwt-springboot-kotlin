package com.rodrigo.authenticationjwt.service

import com.rodrigo.authenticationjwt.model.AuthenticationUser
import com.rodrigo.authenticationjwt.model.User
import com.rodrigo.authenticationjwt.model.UserRequest
import com.rodrigo.authenticationjwt.repository.UserRepository
import com.rodrigo.authenticationjwt.utils.CryptUtil
import com.rodrigo.authenticationjwt.utils.JwtUtil
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.security.Key

@Service
class AuthenticationService(
    @Value("\${jwt.secret}") private val secret: String,
    private val userRepository: UserRepository
) {

    fun signIn(user: UserRequest): AuthenticationUser {
        val userEntity = userRepository.findUserByUsername(user.username) ?: userRepository.save(User(username = user.username, password = CryptUtil.encriptarSiNoEstaEncriptado(user.password)))

        if (!CryptUtil.verificar(user.password, userEntity.password)) {
            throw Exception("Invalid password")
        }

        val claims = Jwts.claims().setSubject(userEntity.id).apply {
            this["userId"] = userEntity.id
        }

        val token = JwtUtil.generarToken(secret, claims, 30)

        return AuthenticationUser(userEntity.username, token)
    }

    fun verify(user: AuthenticationUser): AuthenticationUser {
        var token = user.token

        token = token.replace("Bearer ", "")

        val claims = try {
            val key: Key = Keys.hmacShaKeyFor(secret.toByteArray())
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).body
        } catch (e: ExpiredJwtException) {
            throw Exception("Token expired")
        } catch (e: Exception) {
            throw Exception("Invalid token")
        }

        val userId = claims["userId"] as String

        val userEntity = userRepository.findById(userId).orElseThrow { throw Exception("User not found") }

        return AuthenticationUser(userEntity.username, user.token)
    }

    fun signOut(user: AuthenticationUser) {
        // Do nothing
    }

}