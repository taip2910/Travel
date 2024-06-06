package com.team4studio.travelnow.model.remote.entity

import kotlinx.serialization.Serializable

@Serializable
data class Category(
    var id: String = "",
    val cid: Int = -1,
    val name: String,
) {
    constructor() : this(id = "", cid = -1, name = "")
}