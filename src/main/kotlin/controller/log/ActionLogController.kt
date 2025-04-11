package com.diploma.server.controller.log

import com.diploma.server.controller.user.ActionLogResponse
import com.diploma.server.service.ActionLogService
import com.diploma.server.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.*

@RestController
@RequestMapping("/api/logs")
class ActionLogController (
    private val service: ActionLogService
){
    @GetMapping
    fun listAll(): List<ActionLogResponse> =
        service.findAll()
            .map { it.toResponse() }

    @GetMapping
    fun findByAction(@RequestParam(value = "action", defaultValue = "") action : String): List<ActionLogResponse> =
        service.findByAction(action)
            .map { it.toResponse() }

    @GetMapping
    fun findByUserId(@RequestParam(value = "user_id") userId : String): List<ActionLogResponse> =
        service.findByUserId(UUID.fromString(userId))
            .map { it.toResponse() }

    @GetMapping("/{uuid}")
    fun findByUUID(@PathVariable uuid: UUID): ActionLogResponse =
        service.findByUUID(uuid)
            ?.toResponse()
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Action log not found.")
}