package com.diploma.server.service

import com.diploma.server.model.Role
import com.diploma.server.repository.ActionLogRepository
import com.diploma.server.repository.RoleRepository
import com.diploma.server.service.enums.Actions
import com.diploma.server.service.enums.Entitys
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*

@Service
class RoleService(
    private val repository: RoleRepository,
    private val logService: ActionLogService
) {
    private val logger = LoggerFactory.getLogger(RoleService::class.java)

    fun create(role: Role, username: String): Role? {
        val found = repository.findByName(role.name)

        return if (found == null) {
            if (repository.save(role)) {
                logService.create(
                    username = username,
                    entity = Entitys.ROLE,
                    action = Actions.CREATE,
                    details = "${role.id}(${role.name})"
                )
                logger.info("$username has added a new role(${role.id})")
            }
            role
        } else null
    }

    fun findByName(name: String): Role? =
        repository.findByName(name)

    fun findByUUID(uuid: UUID): Role? =
        repository.findByUUID(uuid)

    fun findAll(): List<Role> =
        repository.findAll()
            .toList()

    fun deleteByUUID(uuid: UUID, username: String): Boolean {
        val response = repository.deleteByUUID(uuid)
        if (response){
            logService.create(
                username = username,
                entity = Entitys.ROLE,
                action = Actions.DELETE,
                details = "$uuid"
            )
            logger.info("$username removed a role(${uuid})")
        }
        return response
    }
}