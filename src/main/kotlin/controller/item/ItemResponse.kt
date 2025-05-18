package com.diploma.server.controller.item

import java.math.BigDecimal
import java.util.*

data class ItemResponse (
    val id: UUID?,
    val name: String,
    val barcode: String,
    val quantity: Int,
    val price: BigDecimal,
    val shopId: UUID? = UUID.fromString("8c5f3a17-9dca-49eb-8f7c-b7de1a5be8f2")
)
