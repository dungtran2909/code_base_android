package com.dungtran.codebase.ui.features.auth.register_profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.PhoneIphone
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dungtran.codebase.R
import com.dungtran.codebase.ui.common.LoadingButton
import com.dungtran.codebase.ui.common.TextFieldWithIcon
import com.dungtran.codebase.ui.common.UserAvatarView
import com.dungtran.codebase.ui.theme.Primary

@Composable
fun RegisterProfileRoute(
    modifier: Modifier = Modifier,
    onRegisterProfileSuccess: () -> Unit,
    onBackToLogin: () -> Unit,
    email: String = "",
    viewModel: RegisterProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isRegisterProfileSuccess) {
        if (uiState.isRegisterProfileSuccess) {
            onRegisterProfileSuccess()
        }
    }
   
    RegisterProfileScreen(
        uiState = uiState,
        onDisplayNameChange = viewModel::onDisplayNameChange,
        onBackToLogin = {
            onBackToLogin()
            viewModel.onCancelRegisterNewUser()
        },
        onCreateUser = viewModel::createUserProfile,
        modifier = modifier
    )
}

@Composable
fun RegisterProfileScreen(
    uiState: RegisterProfileUiState,
    onDisplayNameChange: (String) -> Unit,
    onBackToLogin: () -> Unit,
    onCreateUser: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.background_screen_type_three),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        IconButton(
            modifier = Modifier
                .statusBarsPadding()
                .padding(start = 4.dp),
            onClick = onBackToLogin
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = "Back",
                tint = Color.Black
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            UserAvatarView(
                imageUrl = "https://images2.thanhnien.vn/528068263637045248/2025/9/22/1-1758546255166427717171.jpg",
                size = 150.dp
            )

            Text(
                text = "Profile",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp),
                textAlign = TextAlign.Start
            )

            Text(
                text = "Enter your info profile",
                style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 8.dp),
                textAlign = TextAlign.Start
            )

            TextFieldWithIcon(
                value = uiState.email,
                onValueChange = {},
                readOnly = true,
                label = "Email",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null,
                        tint = Primary
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextFieldWithIcon(
                value = uiState.displayName,
                onValueChange = onDisplayNameChange,
                label = "Name",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = null,
                        tint = Primary
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextFieldWithIcon(
                value = "",
                onValueChange = {},
                label = "Phone number",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.PhoneIphone,
                        contentDescription = null,
                        tint = Primary
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextFieldWithIcon(
                value = "",
                onValueChange = {},
                label = "Location",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.LocationCity,
                        contentDescription = null,
                        tint = Primary
                    )
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            LoadingButton(
                onClick = onCreateUser,
                modifier = Modifier.fillMaxWidth(),
                isLoading = uiState.isLoading,
                enabled = !uiState.isLoading,
                text = "Done"
            )
        }
    }
}