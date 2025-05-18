package com.diploma.server.controller.user

import java.util.*

data class UserResponse(
    val uuid: UUID,
    val username: String,
    val role: String,
    val shopId: UUID?,
)
