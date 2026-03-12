package com.dungtran.codebase.data.remote.dto

import com.squareup.moshi.Json

data class ProductDto(
    @field:Json(name = "id") val id: String,
    @field:Json(name = "name") val name: String,
    @field:Json(name = "price") val price: Double,
)

