package com.dungtran.codebase.domain.usecase

import com.dungtran.codebase.domain.model.Product
import com.dungtran.codebase.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveProductsUseCase @Inject constructor(
    private val repository: ProductRepository,
) {
    operator fun invoke(): Flow<List<Product>> = repository.observeProducts()
}

