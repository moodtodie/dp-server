package com.diploma.server.repository

import com.diploma.server.model.Item
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class ItemRepository {
    private val items = mutableSetOf<Item>()

//    fun save(item: Item): Boolean {
//        return items.add(item)
//    }

    fun save(item: Item): Boolean {
        val foundItem = item.id?.let { findByUUID(it) }

        if (foundItem != null)
            items.removeIf {
                it.id == item.id
            }
        return items.add(item)
    }

    fun findByName(name: String): Item? =
        items
            .firstOrNull { it.name == name }

    fun findByBarcode(barcode: String): Item? =
        items
            .firstOrNull { it.barcode == barcode }

    fun findAll(): Set<Item> =
        items

    fun findByUUID(uuid: UUID): Item? =
        items
            .firstOrNull { it.id == uuid }

    fun deleteByUUID(uuid: UUID): Boolean {
        val foundItem = findByUUID(uuid)

        return foundItem?.let {
            items.removeIf {
                it.id == uuid
            }
        } ?: false
    }
}