package com.dungtran.codebase.ui.features.test_code.work_manager

import com.dungtran.codebase.domain.model.Product

data class WorkManagerUiState (
    val isSyncing: Boolean = false,
    val products: List<Product> = emptyList(),
    val errorMessage: String? = null,
)