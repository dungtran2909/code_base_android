package com.dungtran.codebase.data.mapper

import com.dungtran.codebase.data.local.entity.ProductEntity
import com.dungtran.codebase.data.remote.dto.ProductDto
import com.dungtran.codebase.domain.model.Product

fun ProductEntity.toDomain(): Product = Product(
    id = id,
    name = name,
    price = price,
)

fun ProductDto.toEntity(): ProductEntity = ProductEntity(
    id = id,
    name = name,
    price = price,
)

