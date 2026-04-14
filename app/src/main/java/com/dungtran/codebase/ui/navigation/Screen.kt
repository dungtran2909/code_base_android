package com.dungtran.codebase.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen {
    @Serializable data object Splash : Screen
    @Serializable data object Welcome : Screen
    
    // Screen in Auth
    @Serializable
    data object Login : Screen

    @Serializable
    data object Register : Screen

    @Serializable
    data object ForgotPassword : Screen

    @Serializable
    data class RegisterProfile(
        val email: String = ""
    ) : Screen
    

    @Serializable
    data class DataScreen(
        val userId: String,
        val userName: String = "Guest"
    ) : Screen

    @Serializable data object MainContainer : Screen
    // Screen in Main
    @Serializable data object Chat : Screen
    @Serializable data object Home : Screen
    @Serializable data object Profile : Screen
}