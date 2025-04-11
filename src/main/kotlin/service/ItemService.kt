package com.diploma.server.service

import com.diploma.server.model.Item
import com.diploma.server.repository.ItemRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class ItemService (
    private val itemRepository: ItemRepository
){
    fun createItem(item: Item): Item? {
        val found = itemRepository.findByBarcode(item.barcode)

        return if (found == null) {
            itemRepository.save(item)
            item
        } else null
    }

    fun findByUUID(uuid: UUID): Item? =
        itemRepository.findByUUID(uuid)

    fun findAll(): List<Item> =
        itemRepository.findAll()
            .toList()

    fun deleteByUUID(uuid: UUID): Boolean =
        itemRepository.deleteByUUID(uuid)
}
