package com.diploma.server.repository

import com.diploma.server.model.Item
import com.diploma.server.model.Shop
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.math.BigDecimal
import java.util.UUID

@Repository
//interface ItemRepository : CrudRepository<Item, UUID>
//{
//    fun findByLastName(lastName: String): Iterable<Item>
//}
class ItemRepository{
    private val items = mutableSetOf(
        Item(
            id = UUID.randomUUID(),
            name = "Item1",
            barcode = "12342",
            quantity = 14,
            price = BigDecimal.valueOf(13.5),
            shop = Shop(UUID.randomUUID(),"Shop1","Address1"),
        ),
        Item(
            id = UUID.randomUUID(),
            name = "Item2",
            barcode = "23453",
            quantity = 29,
            price = BigDecimal.valueOf(3.4),
            shop = Shop(UUID.randomUUID(),"Shop2","Address2"),
        ),
    )

    fun save(item: Item): Boolean {
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