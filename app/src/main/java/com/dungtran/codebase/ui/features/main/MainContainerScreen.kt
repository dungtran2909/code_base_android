package com.dungtran.codebase.ui.features.main

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dungtran.codebase.ui.features.main.chat.ChatRoute
import com.dungtran.codebase.ui.features.main.home.HomeRoute
import com.dungtran.codebase.ui.features.main.profile.ProfileRoute
import com.dungtran.codebase.ui.navigation.BottomTab
import com.dungtran.codebase.ui.navigation.Screen

@Composable
fun MainContainerScreen() {
    val childNavController = rememberNavController()
    val navBackStackEntry by childNavController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    Scaffold(
        bottomBar = {
            NavigationBar(
                modifier = Modifier.height(64.dp),
                windowInsets = WindowInsets(0, 0, 0, 0)
            ) {
                BottomTab.entries.forEach { tab ->
                    val isSelected = currentDestination?.hierarchy?.any {
                        it.hasRoute(tab.route::class)
                    } == true

                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            childNavController.navigate(tab.route) {
                                // Tránh tích tụ stack khi bấm tab nhiều lần
                                popUpTo(childNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(tab.icon, contentDescription = tab.label) },
                        /*label = { Text(tab.label) }*/ // Ẩn text của bottom bar
                    )
                }
            }
        }
    ) { innerPadding ->
        // NavHost nội bộ cho 3 Tab
        NavHost(
            navController = childNavController,
            startDestination = Screen.Home,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<Screen.Chat> { ChatRoute() }
            composable<Screen.Home> { HomeRoute() }
            composable<Screen.Profile> { ProfileRoute() }
        }
    }
}