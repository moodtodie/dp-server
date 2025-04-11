package com.diploma.server.repository

import com.diploma.server.model.Shop
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class ShopRepository {
    val shops = mutableSetOf(
        Shop(
            id = UUID.randomUUID(),
            name = "Shop1",
            location = "Address1",
        ),
        Shop(
            id = UUID.randomUUID(),
            name = "Shop2",
            location = "Address2",
        ),
        Shop(
            id = UUID.randomUUID(),
            name = "Shop3",
            location = "Address3",
        ),
    )
    
    fun save(user: Shop): Boolean {
        return shops.add(user)
    }

    fun findByName(name: String): Shop? =
        shops
            .firstOrNull { it.name == name }

    fun findAll(): Set<Shop> =
        shops

    fun findByUUID(uuid: UUID): Shop? =
        shops
            .firstOrNull { it.id == uuid }

    fun deleteByUUID(uuid: UUID): Boolean {
        val found = findByUUID(uuid)

        return found?.let {
            shops.removeIf {
                it.id == uuid
            }
        } ?: false
    }
}