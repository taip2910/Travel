package com.team4studio.travelnow.model.remote.entity

import kotlinx.serialization.Serializable

@Serializable
data class OrderItem(
    val pid: String = "", val quantity: Int = 1, val price: Double
) {
    constructor() : this(pid = "", quantity = 1, price = 0.0)
}
