package com.dungtran.codebase.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dungtran.codebase.ui.features.auth.login.LoginRoute
import com.dungtran.codebase.ui.features.main.MainContainerScreen
import com.dungtran.codebase.ui.features.splash.SplashRoute
import com.dungtran.codebase.ui.features.welcome.WelcomeScreen
import com.dungtran.codebase.ui.features.welcome.WelcomeViewModel

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: Any = Screen.Splash,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable<Screen.Splash> {
            SplashRoute(onTimeout = {
                navController.navigate(Screen.Login) {
                    popUpTo(Screen.Splash) { inclusive = true }
                }
            })
        }

        composable<Screen.Welcome> {
            val welcomeViewModel: WelcomeViewModel = hiltViewModel()
            WelcomeScreen(
                onFinish = {
                    welcomeViewModel.completeWelcome()
                    navController.navigate(Screen.Login) {
                        popUpTo(Screen.Welcome) { inclusive = true }
                    }
                }
            )
        }
        
        // Login Screen
        composable<Screen.Login> {
            LoginRoute(
                modifier = modifier,
                onLoginSuccess = {
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