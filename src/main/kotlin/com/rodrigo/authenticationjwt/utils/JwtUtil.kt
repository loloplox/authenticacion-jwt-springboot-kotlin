package com.rodrigo.authenticationjwt.utils

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import java.security.Key
import java.util.*


object JwtUtil {
    fun generarToken(secretKey: String, claims: Claims?, vigenciaSesion: Int): String {
        val key: Key = Keys.hmacShaKeyFor(secretKey.toByteArray())

        return Jwts.builder()
            .setClaims(claims)
            .setId(UUID.randomUUID().toString()) // ID único del token
            .setIssuedAt(Date()) // Fecha de emisión
            .setExpiration(Date(System.currentTimeMillis() + 60000L * vigenciaSesion)) // Vigencia
            .signWith(key) // Algoritmo de firma
            .compact()
    }
}