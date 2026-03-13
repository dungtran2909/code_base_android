package com.dungtran.codebase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.dungtran.codebase.ui.theme.Code_base_androidTheme
import com.dungtran.codebase.ui.navigation.AppNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    private val viewModel: MainViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        
        /*splashScreen.setKeepOnScreenCondition {
            viewModel.isLoading.value
        }*/

        enableEdgeToEdge()
        
        setContent {
            Code_base_androidTheme {
                // 1. Khởi tạo NavController
                val navController = rememberNavController()
                val startDes by viewModel.startDestination.collectAsStateWithLifecycle()
                
                // 2. Truyền startDestination vào NavHost
                
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavHost(
                        modifier = Modifier.padding(innerPadding),
                        navController = navController,
                        startDestination = startDes
                    )
                }
            }
        }
    }
}