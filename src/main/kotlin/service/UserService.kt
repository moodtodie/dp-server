package com.diploma.server.service

import com.diploma.server.model.User
import com.diploma.server.repository.UserRepository
import com.diploma.server.service.enums.Actions
import com.diploma.server.service.enums.Entitys
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(
    private val userRepository: UserRepository,
    private val logService: ActionLogService
) {
    private val logger = LoggerFactory.getLogger(UserService::class.java)

    fun create(user: User, username: String): User? {
        val found = userRepository.findByUsername(user.username)

        return if (found == null) {
            if (userRepository.save(user)){
                logService.create(
                    username = username,
                    entity = Entitys.USER,
                    action = Actions.CREATE,
                    details = "${user.id}(${user.username})"
                )
                logger.info("$username has added a new user(${user.id})")
            }
            user
        } else null
    }

    fun modify(user: User): Boolean =
        userRepository.save(user)

    fun findByUsername(username: String): User? =
        userRepository.findByUsername(username)

    fun findByUUID(uuid: UUID): User? =
        userRepository.findByUUID(uuid)

    fun findAll(): List<User> =
        userRepository.findAll()
            .toList()

    fun deleteByUUID(uuid: UUID, username: String): Boolean {
        val response = userRepository.deleteByUUID(uuid)
        if (response){
            logService.create(
                username = username,
                entity = Entitys.USER,
                action = Actions.DELETE,
                details = "$uuid"
            )
            logger.info("$username removed a shop(${uuid})")
        }
        return response
    }
}