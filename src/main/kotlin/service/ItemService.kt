package com.diploma.server.service

import com.diploma.server.controller.item.ItemRequest
import com.diploma.server.model.Item
import com.diploma.server.model.Shop
import com.diploma.server.repository.ItemRepository
import com.diploma.server.service.enums.Actions
import com.diploma.server.service.enums.Entitys
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.OkHttpClient
import okhttp3.Request
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.*

@Service
class ItemService(
    private val itemRepository: ItemRepository, private val logService: ActionLogService,
    private val userService: UserService
) {
    private val logger = LoggerFactory.getLogger(ItemService::class.java)

//    DEBUG CODE START
    fun create(item: Item, username: String): Item? {
        val found = itemRepository.findByBarcode(item.barcode)

        return if (found == null) {

            if (itemRepository.save(item)) {
                logService.create(
                    username = username,
                    entity = Entitys.ITEM,
                    action = Actions.CREATE,
                    details = "${item.id}(${item.name})"
                )
                logger.info("$username has added a new item(${item.id})")
            }
            item
        } else null
    }
//    DEBUG CODE END

    fun create(itemRequest: ItemRequest, username: String): Item? {
        val found = itemRepository.findByBarcode(itemRequest.barcode)

        return if (found == null) {

            val shop = userService.findByUsername(username)?.shop

            val item = if (itemRequest.name == null) lookupProduct(itemRequest.barcode, shop)
                ?: return null else itemRequest.toModel(shop)

            if (itemRepository.save(item)) {
                logService.create(
                    username = username,
                    entity = Entitys.ITEM,
                    action = Actions.CREATE,
                    details = "${item.id}(${item.name})"
                )
                logger.info("$username has added a new item(${item.id})")
            }
            item
        } else null
    }

    fun update(item: Item, username: String): Item? {
        val found = itemRepository.findByBarcode(item.barcode)

        return if (found != null) {
            val details = """
                {
                    "id": "${item.id}",
                    "barcode": "${item.barcode}",
                    "oldName": "${found.name}",
                    "newName": "${item.name}",
                    "oldQuantity": ${found.quantity},
                    "newQuantity": ${item.quantity},
                    "oldPrice": ${found.price},
                    "newPrice": ${item.price}
                }
            """.trimIndent()

            if (itemRepository.save(item)) {
                logService.create(
                    username = username, entity = Entitys.ITEM, action = Actions.UPDATE, details = details
                )
                logger.info("$username has update a item(${item.id})")
            }
            item
        } else null
    }

    fun findByUUID(uuid: UUID): Item? = itemRepository.findByUUID(uuid)

    fun findAll(): List<Item> = itemRepository.findAll().toList()

    fun deleteByUUID(uuid: UUID, username: String): Boolean {
        val response = itemRepository.deleteByUUID(uuid)
        if (response) {
            logService.create(
                username = username, entity = Entitys.ITEM, action = Actions.DELETE, details = "$uuid"
            )
            logger.info("$username removed an item(${uuid})")
        }
        return response
    }

    fun lookupProduct(barcode: String, shop: Shop?): Item? {
        val client = OkHttpClient()
        val mapper = jacksonObjectMapper()
        val url = "https://world.openfoodfacts.org/api/v0/product/$barcode.json"
        val request = Request.Builder().url(url).build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                logger.error("Ошибка запроса: ${response.code}")
                return null
            }

            val body = response.body?.string() ?: return null
            val rootNode: JsonNode = mapper.readTree(body)

            val status = rootNode.get("status")?.asInt()
            if (status != 1) {
                logger.error("Товар не найден")
                return null
            }

            val productNode = rootNode.get("product")
            return Item(
                id = UUID.randomUUID(),
                name = productNode?.get("product_name")?.asText().toString(),
//                categories = productNode?.get("categories")?.asText().toString(),
                barcode = barcode,
                quantity = 0,
                price = BigDecimal(0),
                shop = shop,
            )
        }
    }
}

private fun ItemRequest.toModel(shop: Shop?): Item =
    Item(
        id = UUID.randomUUID(),
        name = this.name ?: "unknown",
        barcode = this.barcode,
        quantity = 0,
        price = BigDecimal(0),
        shop = shop,
    )
