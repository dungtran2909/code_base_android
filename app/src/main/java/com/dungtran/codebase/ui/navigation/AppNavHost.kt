package com.dungtran.codebase.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.dungtran.codebase.ui.features.auth.login.LoginRoute
import com.dungtran.codebase.ui.features.auth.register.RegisterRoute
import com.dungtran.codebase.ui.features.auth.register_profile.RegisterProfileRoute
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
                },
                onGotoRegisterProfile = { email ->
                    navController.navigate(Screen.RegisterProfile(email = email)) {
                        popUpTo(Screen.Login) { inclusive = true }
                    }
                },
                onGotoSignup = {
                    navController.navigate(Screen.Register)
                }
            )
        }
        
        // Register Screen
        composable<Screen.Register> { 
            RegisterRoute(
                modifier = modifier,
                onRegisterSuccess = { email ->
                    navController.navigate(Screen.RegisterProfile(email = email)) {
                        popUpTo(Screen.Register) { inclusive = true }
                    }
                }, 
                onBackToLogin = { navController.popBackStack() },
            )
        }

        composable<Screen.RegisterProfile> { backStackEntry ->
            val profileArgs = backStackEntry.toRoute<Screen.RegisterProfile>()
            RegisterProfileRoute(
                modifier = modifier,
                email = profileArgs.email,
                onRegisterProfileSuccess = {
                    navController.navigate(Screen.MainContainer) {
                        popUpTo(Screen.RegisterProfile::class) { inclusive = true }
                    }
                },
                onBackToLogin = {
                    val popped = navController.popBackStack(Screen.Login, inclusive = false)
                    if (!popped) {
                        navController.navigate(Screen.Login) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                }
            )
        }

        // MainContainerScreen
        composable<Screen.MainContainer> {
            MainContainerScreen(
                rootNavController = navController,
                modifier = modifier
            )
        }
    }
}