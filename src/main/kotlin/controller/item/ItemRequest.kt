package com.diploma.server.controller.item

data class ItemRequest(
    val name: String? = null,
    val barcode: String,
    val shopId: String
)
