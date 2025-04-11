package com.diploma.server.controller.auth

data class AuthenticationResponse(
    val accessToken: String,
    val refreshToken: String,
)