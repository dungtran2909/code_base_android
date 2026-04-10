package com.dungtran.codebase.ui.features.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
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
fun MainContainerScreen(
    rootNavController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val childNavController = rememberNavController()
    val navBackStackEntry by childNavController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    Scaffold(
        modifier = modifier,
        bottomBar = {
            // 1. Bọc tất cả trong Surface để tạo bóng đổ (Shadow)
            Surface(
                color = Color.White, // Màu nền của toàn bộ vùng Bottom Bar
                shadowElevation = 8.dp // Tạo bóng đổ nhẹ phía trên để tách biệt với nội dung
            ) {
                Column {
                    HorizontalDivider(
                        thickness = 0.5.dp,
                        color = Color.LightGray.copy(alpha = 0.3f)
                    )
                    NavigationBar(
                        modifier = Modifier.height(64.dp),
                        windowInsets = WindowInsets(0, 0, 0, 0),
                        containerColor = Color.Transparent,
                        tonalElevation = 0.dp
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
                                icon = {
                                    Icon(
                                        painter = painterResource(
                                            id = if (isSelected) tab.selectedIcon else tab.unselectedIcon
                                        ),
                                        contentDescription = tab.label,
                                        tint = Color.Unspecified
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    // Set indicator về Transparent nếu bạn không muốn cái vòng tròn phía sau icon
                                    indicatorColor = Color.Transparent,
                                    // Bạn có thể tùy chỉnh thêm màu sắc tại đây nếu cần
                                )
                                /*label = { Text(tab.label) }*/ // Ẩn text của bottom bar
                            )
                        }
                    }
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
            composable<Screen.Profile> {
                ProfileRoute(
                    onLogout = {
                        rootNavController.navigate(Screen.Login) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}