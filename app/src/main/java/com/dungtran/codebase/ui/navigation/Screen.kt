package com.dungtran.codebase.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen {
    @Serializable
    data object Login : Screen

    @Serializable
    data class Home(
        val userId: String,
        val userName: String = "Guest" // Bạn có thể truyền nhiều tham số cùng lúc
    ) : Screen
}