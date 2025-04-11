package com.diploma.server.controller.auth

data class AuthenticationRequest(
    val email: String,
    val password: String,
)