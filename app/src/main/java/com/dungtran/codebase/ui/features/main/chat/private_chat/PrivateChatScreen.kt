package com.dungtran.codebase.ui.features.main.chat.private_chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dungtran.codebase.ui.common.UserAvatarView
import com.dungtran.codebase.ui.navigation.Screen

@Composable
fun PrivateChatRoute(
    modifier: Modifier = Modifier,
    viewModel: PrivateChatViewModel = hiltViewModel(),
    onBack: () -> Unit, 
    dataScreen: Screen.PrivateChat
) {
    LaunchedEffect(dataScreen) { 
        viewModel.initData(dataScreen)
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    PrivateChatScreen(
        modifier = modifier,
        uiState = uiState,
        onBack = onBack
    )
}

@Composable
fun PrivateChatScreen(
    modifier: Modifier = Modifier,
    uiState: PrivateChatUiState, 
    onBack: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(color = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .height(56.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }

            UserAvatarView(
                imageUrl = uiState.dataScreen?.partnerAvatar,
                size = 30.dp,
                borderWidth = 0.dp
            )

            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = uiState.dataScreen?.partnerName?: "Người dùng",
                style = MaterialTheme.typography.labelMedium.copy(
                    color = Color.Black, 
                    fontWeight = FontWeight.Black
                )
            )
        }
        
        Box(modifier = Modifier.fillMaxSize().padding(top = 56.dp)) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .background(Color.White),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Private Chat Screen",
                    style = MaterialTheme.typography.headlineMedium
                )
            }   
        }
    }
}