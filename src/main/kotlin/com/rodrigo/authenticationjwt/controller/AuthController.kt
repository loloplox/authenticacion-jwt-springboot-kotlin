package com.rodrigo.authenticationjwt.controller

import com.rodrigo.authenticationjwt.model.AuthenticationUser
import com.rodrigo.authenticationjwt.model.UserRequest
import com.rodrigo.authenticationjwt.service.AuthenticationService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authenticationService: AuthenticationService
) {

    @PostMapping("/sign-in")
    fun signIn(@RequestBody user: UserRequest): ResponseEntity<AuthenticationUser> {
        return ResponseEntity.ok(authenticationService.signIn(user))
    }

    @PostMapping("/verify")
    fun verify(@RequestBody user: AuthenticationUser): ResponseEntity<AuthenticationUser> {
        return ResponseEntity.ok(authenticationService.verify(user))
    }
}