package com.diploma.server.controller.role

import com.diploma.server.model.Role
import com.diploma.server.service.RoleService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.*

@RestController
@RequestMapping("/api/roles")
class RoleController(
    private val service: RoleService
) {
    @PostMapping
    fun create(@RequestBody roleRequest: RoleRequest): RoleResponse {
        val principal = SecurityContextHolder.getContext().authentication.principal
        val username = if (principal is UserDetails) principal.username else "unknown"

        return service.create(roleRequest.toModel(), username)
            ?.toResponse()
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot create role.")
    }

    @GetMapping
    fun listAll(): List<RoleResponse> =
        service.findAll()
            .map { it.toResponse() }

    @GetMapping("/{uuid}")
    fun findByUUID(@PathVariable uuid: UUID): RoleResponse =
        service.findByUUID(uuid)
            ?.toResponse()
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found.")

    @DeleteMapping("/{uuid}")
    fun deleteByUUID(@PathVariable uuid: UUID): ResponseEntity<Boolean> {
        val principal = SecurityContextHolder.getContext().authentication.principal
        val username = if (principal is UserDetails) principal.username else "unknown"

        val success = service.deleteByUUID(uuid, username)

        return if (success)
            ResponseEntity.noContent()
                .build()
        else
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found.")
    }
}

private fun Role.toResponse(): RoleResponse =
    RoleResponse(
        name = this.name,
    )

private fun RoleRequest.toModel(): Role =
    Role(
        id = UUID.randomUUID(),
        name = this.name,
    )