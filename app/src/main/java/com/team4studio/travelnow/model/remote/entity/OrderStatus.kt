package com.team4studio.travelnow.model.remote.entity


@kotlinx.serialization.Serializable
enum class OrderStatus(code: String)  {
    All("All"),
    Processing("Đang Xử Lý"),
    Shipped("Đang Xác Nhận"),
    Delivered("Đã Xác Nhận"),
}