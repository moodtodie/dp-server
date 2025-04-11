package com.diploma.server.repository

import com.diploma.server.model.Role
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class RoleRepository {
    private val roles = mutableSetOf(
        Role(
            id = UUID.randomUUID(),
            name = "USER",
        ),
        Role(
            id = UUID.randomUUID(),
            name = "ADMIN",
        ),
    )

    fun save(role: Role): Boolean {
        return roles.add(role)
    }

    fun findByName(name: String): Role? =
        roles
            .firstOrNull { it.name == name }

    fun findAll(): Set<Role> =
        roles

    fun findByUUID(uuid: UUID): Role? =
        roles
            .firstOrNull { it.id == uuid }

    fun deleteByUUID(uuid: UUID): Boolean {
        val foundRole = findByUUID(uuid)

        return foundRole?.let {
            roles.removeIf {
                it.id == uuid
            }
        } ?: false
    }
}