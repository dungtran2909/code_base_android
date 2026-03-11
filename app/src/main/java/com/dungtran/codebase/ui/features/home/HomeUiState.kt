package com.dungtran.codebase.ui.features.home

import com.dungtran.codebase.domain.model.Product

data class HomeUiState(
    val isSyncing: Boolean = false,
    val products: List<Product> = emptyList(),
    val errorMessage: String? = null,
)

