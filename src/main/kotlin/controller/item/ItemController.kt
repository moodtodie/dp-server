package com.diploma.server.controller.item

import com.diploma.server.model.Item
import com.diploma.server.model.Shop
import com.diploma.server.service.ItemService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.math.BigDecimal
import java.util.*

@RestController
@RequestMapping("/api/items")
class ItemController(
    private val itemService: ItemService
) {
    @PostMapping
    fun create(@RequestBody userRequest: ItemRequest): ItemResponse {
        val principal = SecurityContextHolder.getContext().authentication.principal
        val username = if (principal is UserDetails) principal.username else "unknown"

        return itemService.create(userRequest, username)
            ?.toResponse()
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot create item.")
    }

    @GetMapping
    fun listAll(): List<ItemResponse> {
        return itemService.findAll()
            .map { it.toResponse() }
    }

    @GetMapping("/{uuid}")
    fun findByUUID(@PathVariable uuid: UUID): ItemResponse =
        itemService.findByUUID(uuid)
            ?.toResponse()
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found.")

    @DeleteMapping("/{uuid}")
    fun deleteByUUID(@PathVariable uuid: UUID): ResponseEntity<Boolean> {
        val principal = SecurityContextHolder.getContext().authentication.principal
        val username = if (principal is UserDetails) principal.username else "unknown"

        val success = itemService.deleteByUUID(uuid, username)

        return if (success)
            ResponseEntity.noContent()
                .build()
        else
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found.")
    }
}

private fun Item.toResponse(): ItemResponse =
    ItemResponse(
        id = this.id,
        name = this.name,
        barcode = this.barcode,
        quantity = this.quantity,
        price = this.price,
    )

//private fun ItemRequest.toModel(): Item =
//    Item(
//        id = UUID.randomUUID(),
//        name = this.name,
//        barcode = this.barcode,
//        quantity = 0,
//        price = BigDecimal(0),
////        shop = shopService.findByUUID(this.shopId),
//        shop = Shop(UUID.randomUUID(), "Shop1", "Address1"),
//    )
