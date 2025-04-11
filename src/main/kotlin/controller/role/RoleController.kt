package com.diploma.server.controller.role

import com.diploma.server.controller.user.UserRequest
import com.diploma.server.controller.user.UserResponse
import com.diploma.server.service.ActionLogService
import com.diploma.server.service.RoleService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.*

@RestController
@RequestMapping("/api/roles")
class RoleController (
    private val service: RoleService
){
    @PostMapping
    fun create(@RequestBody roleRequest: RoleRequest): UserResponse =
        service.create(roleRequest.toModel())
            ?.toResponse()
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot create role.")

    @GetMapping
    fun listAll(): List<RoleResponse> =
        service.findAll()
            .map { it.toResponse() }

    @GetMapping("/{uuid}")
    fun findByUUID(@PathVariable uuid: UUID): UserResponse =
        service.findByUUID(uuid)
            ?.toResponse()
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found.")

    @DeleteMapping("/{uuid}")
    fun deleteByUUID(@PathVariable uuid: UUID): ResponseEntity<Boolean> {
        val success = service.deleteByUUID(uuid)

        return if (success)
            ResponseEntity.noContent()
                .build()
        else
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found.")
    }
}