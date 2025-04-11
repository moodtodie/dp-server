package com.diploma.server.controller.shop

import com.diploma.server.controller.user.UserResponse
import com.diploma.server.service.RoleService
import com.diploma.server.service.ShopService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.*

@RestController
@RequestMapping("/api/shops")
class ShopController (
    private val service: ShopService
){
    @PostMapping
    fun create(@RequestBody ShopRequest: ShopRequest): UserResponse =
        service.create(ShopRequest.toModel())
            ?.toResponse()
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot create role.")

    @GetMapping
    fun listAll(): List<ShopResponse> =
        service.findAll()
            .map { it.toResponse() }

    @GetMapping
    fun findByName(@RequestParam(value = "name") name : String): List<ShopResponse> =
        service.findByName(name)
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