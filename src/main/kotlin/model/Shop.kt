package com.diploma.server.model

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "shop")
data class Shop(
    @Id
    @GeneratedValue
    val id: UUID? = null,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    val location: String
)