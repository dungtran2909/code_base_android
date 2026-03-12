package com.dungtran.codebase.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dungtran.codebase.ui.features.auth.login.LoginRoute
import com.dungtran.codebase.ui.features.main.MainContainerScreen
import com.dungtran.codebase.ui.features.splash.SplashRoute

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash,
    ) {
        composable<Screen.Splash> {
            SplashRoute(onTimeout = {
                navController.navigate(Screen.Login) {
                    popUpTo(Screen.Splash) { inclusive = true }
                }
            })
        }
        
        // Login Screen
        composable<Screen.Login> {
            LoginRoute(
                modifier = modifier,
                onLoginSuccess = {
                    // Truyền tham số cực kỳ an toàn và dễ hiểu
                    navController.navigate(Screen.MainContainer) {
                        popUpTo(Screen.Login) { inclusive = true }
                    }
                }
            )
        }

        // MainContainerScreen
        composable<Screen.MainContainer> {
            MainContainerScreen(modifier = modifier)
        }
    }
}