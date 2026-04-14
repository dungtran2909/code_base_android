package com.dungtran.codebase.ui.features.main.chat

import com.dungtran.codebase.domain.model.User

data class ChatUiState(
    val isLoading: Boolean = false,
    val users: List<User> = emptyList(), 
    val errorMessage: String? = null,
    val searchText: String = ""
)