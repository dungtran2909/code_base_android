package com.dungtran.codebase.ui.features.auth.register_profile

data class RegisterProfileUiState(
    val displayName: String = "",
    val email: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isRegisterProfileSuccess: Boolean = false
)