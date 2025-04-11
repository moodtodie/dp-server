package com.diploma.server.model

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "role")
data class Role(
    @Id
    @GeneratedValue
    val id: UUID? = null,

    @Column(unique = true, nullable = false)
    val name: String,

//    @Column(nullable = false, columnDefinition = "jsonb")
//    val permissions: String // JSONB хранит права доступа
)

enum class RoleTmp{
    USER, ADMIN
}