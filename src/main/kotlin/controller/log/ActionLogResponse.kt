package com.diploma.server.controller.log

import java.sql.Timestamp
import java.util.UUID

data class ActionLogResponse(
    val logUUID: UUID?,
    val createdAt: Timestamp,
    val createdBy: String,
    val entity: String,
    val action: String,
    val details: String,
)
