package com.diploma.server.controller.log

import com.diploma.server.model.ActionLog
import com.diploma.server.service.ActionLogService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.*

@RestController
@RequestMapping("/api/logs")
class ActionLogController(
    private val service: ActionLogService
) {
    @GetMapping
    fun getLogs(
        @RequestParam(value = "action", required = false) action: String?,
        @RequestParam(value = "user_id", required = false) userId: String?
    ): List<ActionLogResponse> {
        return when {
            action != null -> service.findByAction(action).map { it.toResponse() }
            userId != null -> service.findByUserId(UUID.fromString(userId)).map { it.toResponse() }
            else -> service.findAll().map { it.toResponse() }
        }
    }

    @GetMapping("/{uuid}")
    fun findByUUID(@PathVariable uuid: UUID): ActionLogResponse =
        service.findByUUID(uuid)
            ?.toResponse()
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Action log not found.")
}

private fun ActionLog.toResponse(): ActionLogResponse =
    ActionLogResponse(
        logUUID = this.id,
        createdAt = this.createdAt,
        createdBy = this.createdBy.username,
        action = this.action,
        entity = this.entity,
        details = this.details,
    )