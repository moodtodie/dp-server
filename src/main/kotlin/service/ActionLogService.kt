package com.diploma.server.service

import com.diploma.server.model.ActionLog
import com.diploma.server.repository.ActionLogRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class ActionLogService (
    private val repository: ActionLogRepository
){
    fun create(log: ActionLog): ActionLog? {
        repository.save(log)
        return log
    }

    fun findByAction(action: String): ActionLog? =
        repository.findByAction(action)

    fun findByUserId(userId: UUID): ActionLog? =
        repository.findByUserId(userId)

    fun findByUUID(uuid: UUID): ActionLog? =
        repository.findByUUID(uuid)

    fun findAll(): List<ActionLog> =
        repository.findAll()
            .toList()
}