package com.diploma.server.service

import com.diploma.server.model.ActionLog
import com.diploma.server.repository.ActionLogRepository
import com.diploma.server.repository.UserRepository
import com.diploma.server.service.enums.Actions
import com.diploma.server.service.enums.Entitys
import org.springframework.stereotype.Service
import java.util.*

@Service
class ActionLogService(
    private val repository: ActionLogRepository,
    private val userRepository: UserRepository
) {
    fun create(username: String, entity: Entitys, action: Actions, details: String = ""): ActionLog? {
        val log =
            userRepository.findByUsername(username)?.let {
                ActionLog(
                    id = UUID.randomUUID(),
                    createdBy = it,
                    entity = entity.name,
                    action = action.name,
                    details = details
                )
            }
        log?.let { repository.save(it) }
        return log
    }

    fun findByAction(action: String): List<ActionLog> =
        repository.findByAction(action).toList()

    fun filter(form: Date? = null, to: Date? = null, entity: String? = null , action: String? = null): List<ActionLog> =
        repository.findWithFilter(
            form = form,
            to = to,
            entity = entity,
            action = action
        ).toList()

    fun findByUserId(userId: UUID): List<ActionLog> =
        repository.findByUserId(userId).toList()

    fun findByUUID(uuid: UUID): ActionLog? =
        repository.findByUUID(uuid)

    fun findAll(): List<ActionLog> =
        repository.findAll().toList()
}