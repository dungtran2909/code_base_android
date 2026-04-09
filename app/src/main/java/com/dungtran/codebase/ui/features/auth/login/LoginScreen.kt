package com.dungtran.codebase.ui.features.auth.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dungtran.codebase.R

@Composable
fun LoginRoute(
    onLoginSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isLoginSuccess) {
        if (uiState.isLoginSuccess) {
            onLoginSuccess()
        }
    }

    LoginScreen(
        uiState = uiState,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onLoginClick = viewModel::login,
        modifier = modifier
    )
}

@Composable
fun LoginScreen(
    uiState: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.background_screen_type_three),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(), 
            contentScale = ContentScale.Crop
        )
        
        Scaffold(
            modifier = modifier, 
            containerColor = Color.Transparent
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon_app),
                    modifier = Modifier.size(100.dp),
                    contentDescription = null,
                    contentScale = ContentScale.Inside
                )

                Text(
                    text = "Mixi Vivu",
                    style = MaterialTheme.typography.headlineMedium,
                    fontSize = 30.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Text(
                    text = "App base made by Dũng Trần",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        platformStyle = PlatformTextStyle(includeFontPadding = false),
                        color = Color.Gray,
                    ),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
                
                Text(
                    text = "Đăng Nhập",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = uiState.email,
                    onValueChange = onEmailChange,
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = uiState.password,
                    onValueChange = onPasswordChange,
                    label = { Text("Mật khẩu") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )

                if (uiState.errorMessage != null) {
                    Text(
                        text = uiState.errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = onLoginClick,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !uiState.isLoading
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Đăng nhập")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Màn hình Login đang Loading")
@Composable
fun LoginScreenLoadingPreview() {
    // Mock state cho trường hợp đang đăng nhập
    val mockUiState = LoginUiState(
        email = "user@example.com",
        password = "password123",
        isLoading = true,
        errorMessage = null,
        isLoginSuccess = false
    )

    LoginScreen(
        uiState = mockUiState,
        onEmailChange = {},
        onPasswordChange = {},
        onLoginClick = {},
        modifier = Modifier
    )
}

/*
@Preview(showBackground = true, name = "Màn hình Login có lỗi")
@Composable
fun LoginScreenErrorPreview() {
    // Mock state cho trường hợp báo lỗi
    val mockUiState = LoginUiState(
        email = "sai-email",
        password = "123",
        isLoading = false,
        errorMessage = "Email hoặc mật khẩu không đúng",
        isLoginSuccess = false
    )

    LoginScreen(
        uiState = mockUiState,
        onEmailChange = {},
        onPasswordChange = {},
        onLoginClick = {},
        modifier = Modifier
    )
}*/
