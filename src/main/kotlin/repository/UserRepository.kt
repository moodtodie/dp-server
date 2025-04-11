package com.diploma.server.repository

import com.diploma.server.model.Role
import com.diploma.server.model.RoleTmp
import com.diploma.server.model.Shop
import com.diploma.server.model.User
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class UserRepository(
    private val encoder: PasswordEncoder
) {
    private val users = mutableSetOf(
        User(
            id = UUID.randomUUID(),
            email = "email-1@gmail.com",
            password = encoder.encode("pass1"),
            role = RoleTmp.USER,
            shop = Shop(UUID.randomUUID(),"Shop1","Address1"),
        ),
        User(
            id = UUID.randomUUID(),
            email = "email-2@gmail.com",
            password = encoder.encode("pass2"),
            role = RoleTmp.ADMIN,
            shop = Shop(UUID.randomUUID(),"Shop1","Address1"),
        ),
        User(
            id = UUID.randomUUID(),
            email = "email-3@gmail.com",
            password = encoder.encode("pass3"),
            role = RoleTmp.USER,
            shop = Shop(UUID.randomUUID(),"Shop1","Address1"),
        ),
    )

    fun save(user: User): Boolean {
        val updated = user.copy(password = encoder.encode(user.password))

        return users.add(updated)
    }

    fun findByEmail(email: String): User? =
        users
            .firstOrNull { it.email == email }

    fun findAll(): Set<User> =
        users

    fun findByUUID(uuid: UUID): User? =
        users
            .firstOrNull { it.id == uuid }

    fun deleteByUUID(uuid: UUID): Boolean {
        val foundUser = findByUUID(uuid)

        return foundUser?.let {
            users.removeIf {
                it.id == uuid
            }
        } ?: false
    }
}