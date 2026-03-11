package com.dungtran.codebase.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen {
    @Serializable
    data object Login : Screen

    @Serializable
    data class DataScreen(
        val userId: String,
        val userName: String = "Guest" // Bạn có thể truyền nhiều tham số cùng lúc
    ) : Screen

    @Serializable data object MainContainer : Screen
    // Các tab bên trong BottomBar
    @Serializable data object Chat : Screen
    @Serializable data object Home : Screen
    @Serializable data object Profile : Screen
}