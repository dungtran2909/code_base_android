package com.dungtran.codebase.domain.usecase

import com.dungtran.codebase.domain.repository.ProductRepository
import javax.inject.Inject

class SyncProductsUseCase @Inject constructor(
    private val repository: ProductRepository,
) {
    suspend operator fun invoke() = repository.syncProducts()
}

