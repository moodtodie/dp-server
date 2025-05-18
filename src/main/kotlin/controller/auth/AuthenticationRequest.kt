package com.diploma.server.controller.auth

data class AuthenticationRequest(
    val username: String,
    val password: String,
)