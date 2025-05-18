package com.diploma.server.repository

import com.diploma.server.model.Shop
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class ShopRepository {
    val shops = mutableSetOf<Shop>()

    fun save(user: Shop): Boolean {
        return shops.add(user)
    }

    fun findByName(name: String): Set<Shop> =
        shops.filter { it.name == name }.toSet()

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