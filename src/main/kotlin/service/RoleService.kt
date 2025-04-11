package com.diploma.server.service

import com.diploma.server.model.Role
import com.diploma.server.repository.RoleRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class RoleService (
    private val repository: RoleRepository
) {
    fun create(role: Role): Role? {
        val found = repository.findByName(role.name)

        return if (found == null) {
            repository.save(role)
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

    fun deleteByUUID(uuid: UUID): Boolean =
        repository.deleteByUUID(uuid)
}