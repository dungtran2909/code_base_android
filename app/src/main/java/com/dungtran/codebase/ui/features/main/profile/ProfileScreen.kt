package com.dungtran.codebase.ui.features.main.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ProfileRoute(
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val isLogoutSuccess by viewModel.isLogoutSuccess.collectAsStateWithLifecycle()
    
    LaunchedEffect(isLogoutSuccess) {
        if (isLogoutSuccess) {
            onLogout()
        }
    }
    
    ProfileScreen(
        onLogoutClick = viewModel::logout,
        modifier = modifier
    )
}

@Composable
fun ProfileScreen(
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "Profile Screen",
            style = MaterialTheme.typography.bodyMedium,
        )

        TextButton(
            onClick = onLogoutClick,
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = "Logout",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Red 
            )
        }
    }
}