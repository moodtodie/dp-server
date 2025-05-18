package com.diploma.server.controller.user

import com.diploma.server.model.RoleTmp
import com.diploma.server.model.Shop
import com.diploma.server.model.User
import com.diploma.server.service.UserService
import org.apache.tomcat.util.net.openssl.ciphers.Authentication
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.*

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService
) {
    @PostMapping
    fun create(@RequestBody userRequest: UserRequest): UserResponse {
        val principal = SecurityContextHolder.getContext().authentication.principal
        val username = if (principal is UserDetails) principal.username else "unknown"

        return userService.create(userRequest.toModel(), username)
            ?.toResponse()
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot create user.")
    }

    @GetMapping
    fun listAll(): List<UserResponse> =
        userService.findAll()
            .map { it.toResponse() }

    @GetMapping("/{uuid}")
    fun findByUUID(@PathVariable uuid: UUID): UserResponse =
        userService.findByUUID(uuid)
            ?.toResponse()
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.")

    @DeleteMapping("/{uuid}")
    fun deleteByUUID(@PathVariable uuid: UUID): ResponseEntity<Boolean> {
        val principal = SecurityContextHolder.getContext().authentication.principal
        val username = if (principal is UserDetails) principal.username else "unknown"

        val success = userService.deleteByUUID(uuid, username)

        return if (success)
            ResponseEntity.noContent()
                .build()
        else
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.")
    }

    @PostMapping("/change-password")
    fun changePassword(
        @RequestBody request: ChangePasswordRequest,
        authentication: Authentication,
        passwordEncoder: PasswordEncoder
    ): ResponseEntity<String> {
        val username = authentication.name
        val user = userService.findByUsername(username)
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Пользователь не найден")

        if (!passwordEncoder.matches(request.oldPassword, user.password)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Неверный текущий пароль")
        }

        userService.modify(user.copy(password = request.newPassword))

        return ResponseEntity.ok("Пароль успешно изменён")
    }

    private fun User.toResponse(): UserResponse =
        UserResponse(
            uuid = this.id,
            username = this.username,
            role = this.role.name,
            shopId = this.shop?.id,
        )

    private fun UserRequest.toModel(): User =
        User(
            id = UUID.randomUUID(),
            username = this.username,
            password = this.password,
            role = RoleTmp.USER,
            shop = Shop(UUID.randomUUID(), "Shop1", "Address1"),
        )
}
