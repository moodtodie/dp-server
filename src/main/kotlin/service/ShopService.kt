package com.diploma.server.service

import com.diploma.server.model.Shop
import com.diploma.server.repository.ShopRepository
import com.diploma.server.service.enums.Actions
import com.diploma.server.service.enums.Entitys
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*

@Service
class ShopService(
    private val repository: ShopRepository,
    private val logService: ActionLogService
) {
    private val logger = LoggerFactory.getLogger(ShopService::class.java)

    fun create(shop: Shop, username: String): Shop? {
        val found = repository.findByName(shop.name)

        return if (found.isEmpty()) {
            if(repository.save(shop)){
                logService.create(
                    username = username,
                    entity = Entitys.SHOP,
                    action = Actions.CREATE,
                    details = "${shop.id}(${shop.name})"
                )
                logger.info("$username has added a new shop(${shop.id})")
            }
            shop
        } else null
    }

    fun findByName(name: String): List<Shop> =
        repository.findByName(name).toList()

    fun findByUUID(uuid: UUID): Shop? =
        repository.findByUUID(uuid)

    fun findAll(): List<Shop> =
        repository.findAll().toList()

    fun deleteByUUID(uuid: UUID, username: String): Boolean {
        val response = repository.deleteByUUID(uuid)
        if (response){
            logService.create(
                username = username,
                entity = Entitys.SHOP,
                action = Actions.DELETE,
                details = "$uuid"
            )
            logger.info("$username removed a shop(${uuid})")
        }
        return response
    }
}