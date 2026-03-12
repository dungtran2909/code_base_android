package com.dungtran.codebase.domain.repository

import com.dungtran.codebase.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun observeProducts(): Flow<List<Product>>
    suspend fun syncProducts(): Result<Unit>
}

