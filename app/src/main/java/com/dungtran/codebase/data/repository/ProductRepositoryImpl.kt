package com.dungtran.codebase.data.repository

import com.dungtran.codebase.data.local.dao.ProductDao
import com.dungtran.codebase.data.mapper.toDomain
import com.dungtran.codebase.data.mapper.toEntity
import com.dungtran.codebase.data.remote.api.ProductApi
import com.dungtran.codebase.domain.model.Product
import com.dungtran.codebase.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val api: ProductApi,
    private val dao: ProductDao,
) : ProductRepository {

    override fun observeProducts(): Flow<List<Product>> =
        dao.observeProducts().map { entities -> entities.map { it.toDomain() } }

    override suspend fun syncProducts(): Result<Unit> =
        runCatching {
            val remote = api.getProducts()
            dao.upsertAll(remote.map { it.toEntity() })
        }
}

