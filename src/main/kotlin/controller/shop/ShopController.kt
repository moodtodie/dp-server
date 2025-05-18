package com.diploma.server.controller.shop

import com.diploma.server.model.Shop
import com.diploma.server.service.ShopService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.*

@RestController
@RequestMapping("/api/shops")
class ShopController(
    private val service: ShopService
) {
    @PostMapping
    fun create(@RequestBody shopRequest: ShopRequest): ShopResponse {
        val principal = SecurityContextHolder.getContext().authentication.principal
        val username = if (principal is UserDetails) principal.username else "unknown"

        return service.create(shopRequest.toModel(), username)
            ?.toResponse()
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot create shop.")
    }

    @GetMapping
    fun getShops(
        @RequestParam(value = "name", required = false) name: String?
    ): List<ShopResponse> {
        return when {
            name != null -> service.findByName(name).map { it.toResponse() }
            else -> service.findAll().map { it.toResponse() }
        }
    }

    @GetMapping("/{uuid}")
    fun findByUUID(@PathVariable uuid: UUID): ShopResponse =
        service.findByUUID(uuid)
            ?.toResponse()
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Shop not found.")

    @DeleteMapping("/{uuid}")
    fun deleteByUUID(@PathVariable uuid: UUID): ResponseEntity<Boolean> {
        val principal = SecurityContextHolder.getContext().authentication.principal
        val username = if (principal is UserDetails) principal.username else "unknown"

        val success = service.deleteByUUID(uuid, username)

        return if (success)
            ResponseEntity.noContent()
                .build()
        else
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Shop not found.")
    }
}

private fun Shop.toResponse(): ShopResponse =
    ShopResponse(
        uuid = this.id,
        name = this.name,
        location = this.location
    )

private fun ShopRequest.toModel(): Shop =
    Shop(
        id = UUID.randomUUID(),
        name = this.name,
        location = this.location,
    )