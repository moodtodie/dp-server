package com.diploma.server.controller.user

data class ChangePasswordRequest(
    val oldPassword: String,
    val newPassword: String
)