package com.dungtran.codebase.ui.features.main.profile

import com.dungtran.codebase.domain.model.User

data class ProfileUiState(
    val myUid: String = "",
    val userProfile: User? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)