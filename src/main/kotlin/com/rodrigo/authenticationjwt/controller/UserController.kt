package com.rodrigo.authenticationjwt.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user")
class UserController {

    @GetMapping
    fun getAllUsers(): ResponseEntity<String> {
        return ResponseEntity.ok("All users")
    }

}