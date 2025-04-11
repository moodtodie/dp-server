package com.diploma.server.service

import com.diploma.server.model.Shop
import com.diploma.server.repository.ShopRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class ShopService (
    private val repository: ShopRepository
) {
    fun create(shop: Shop): Shop? {
        val found = repository.findByName(shop.name)

        return if (found == null) {
            repository.save(shop)
            shop
        } else null
    }

    fun findByName(name: String): Shop? =
        repository.findByName(name)

    fun findByUUID(uuid: UUID): Shop? =
        repository.findByUUID(uuid)

    fun findAll(): List<Shop> =
        repository.findAll()
            .toList()

    fun deleteByUUID(uuid: UUID): Boolean =
        repository.deleteByUUID(uuid)
}