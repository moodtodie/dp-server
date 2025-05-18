package com.diploma.server.model

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "user")
data class User (
    @Id
    @GeneratedValue
    val id: UUID,

    @Column(unique = true, nullable = false)
    val username: String,

    @Column(nullable = false)
    val password: String,

//    @ManyToOne
//    @JoinColumn(name = "role_id", nullable = false)
//    val role: Role,

    val role: RoleTmp,

    @ManyToOne
    @JoinColumn(name = "shop_id", nullable = true)
    val shop: Shop?
)
