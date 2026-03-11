package com.dungtran.codebase.ui.navigation

import com.dungtran.codebase.R

enum class BottomTab(
    val route: Any,
    val selectedIcon: Int,
    val unselectedIcon: Int,
    val label: String
) {
    CHAT(
        Screen.Chat,
        R.drawable.ic_chat_tab_selected,
        R.drawable.ic_chat_tab_unselected,
        "Chat"
    ),
    HOME(
        Screen.Home,
        R.drawable.ic_home_tab_selected,
        R.drawable.ic_home_tab_unselected,
        "Main"
    ),
    PROFILE(
        Screen.Profile,
        R.drawable.ic_profile_tab_selected,
        R.drawable.ic_profile_tab_unselected,
        "Profile"
    )
}