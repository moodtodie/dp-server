package com.diploma.server.repository

import com.diploma.server.model.ActionLog
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class ActionLogRepository {
    private val logs = mutableSetOf<ActionLog>()
    private val logger = LoggerFactory.getLogger(ActionLogRepository::class.java)

    fun save(log: ActionLog): Boolean {
        logger.info("New action logged. ${log.createdBy.username} ${log.action} ${log.entity}")
        return logs.add(log)
    }

    fun findWithFilter(form: Date? = null, to: Date? = null, entity: String? = null , action: String? = null): Set<ActionLog> =
        logs.filter { it.action == action }.toSet()

    fun findByAction(action: String): Set<ActionLog> =
        logs.filter { it.action == action }.toSet()

    fun findByUserId(userId: UUID): Set<ActionLog> =
        logs.filter { it.createdBy.id == userId }.toSet()

    fun findAll(): Set<ActionLog> =
        logs

    fun findByUUID(uuid: UUID): ActionLog? =
        logs
            .firstOrNull { it.id == uuid }
}