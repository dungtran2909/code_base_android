package com.dungtran.codebase.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

enum class BottomTab(
    val route: Any,
    val icon: ImageVector,
    val label: String
) {
    CHAT(Screen.Chat, Icons.Default.Chat, "Chat"),
    HOME(Screen.Home, Icons.Default.Home, "Home"),
    PROFILE(Screen.Profile, Icons.Default.Person, "Profile")
}