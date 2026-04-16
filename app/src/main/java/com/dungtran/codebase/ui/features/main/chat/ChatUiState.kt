package com.dungtran.codebase.ui.features.main.chat

import com.dungtran.codebase.domain.model.Chat
import com.dungtran.codebase.domain.model.User

data class ChatUiState(
    val myUid: String = "",
    val users: List<User> = emptyList(),
    val chats: List<Chat> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val searchText: String = ""
)