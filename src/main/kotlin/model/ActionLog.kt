package com.diploma.server.model

import jakarta.persistence.*
import java.sql.Timestamp
import java.util.*

@Entity
@Table(name = "action_logs")
data class ActionLog(
    @Id
    @GeneratedValue
    val id: UUID? = null,

    @Column(name = "created_at", nullable = false)
    val createdAt: Timestamp = Timestamp(System.currentTimeMillis()),

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val createdBy: User,

    @Column(nullable = false)
    val entity: String, // Например, "item" или "shop"

    @Column(nullable = false)
    val action: String, // Например, "CREATE", "UPDATE", "DELETE"

    @Column(nullable = false)
    val details: String // Что именно изменилось
)
