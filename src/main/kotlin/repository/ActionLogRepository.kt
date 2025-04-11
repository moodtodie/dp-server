package com.diploma.server.repository

import com.diploma.server.model.ActionLog
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class ActionLogRepository {
    private val logs = mutableSetOf<ActionLog>()

    fun save(log: ActionLog): Boolean {
        return logs.add(log)
    }

    fun findByAction(action: String): ActionLog? =
        logs
            .firstOrNull { it.action == action }

    fun findByUserId(userId: UUID): ActionLog? =
        logs
            .firstOrNull { it.user.id == userId }

    fun findAll(): Set<ActionLog> =
        logs

    fun findByUUID(uuid: UUID): ActionLog? =
        logs
            .firstOrNull { it.id == uuid }
}