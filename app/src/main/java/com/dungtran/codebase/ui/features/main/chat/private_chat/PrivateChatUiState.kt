package com.dungtran.codebase.ui.features.main.chat.private_chat

import com.dungtran.codebase.domain.model.Message
import com.dungtran.codebase.ui.navigation.Screen


data class PrivateChatUiState(
    val myUid: String = "",
    val dataScreen: Screen.PrivateChat? = null,
    val messageList: List<Message> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)