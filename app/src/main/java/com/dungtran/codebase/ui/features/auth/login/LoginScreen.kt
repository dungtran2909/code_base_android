package com.dungtran.codebase.ui.features.auth.login

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.NoCredentialException
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dungtran.codebase.R
import com.dungtran.codebase.ui.common.*
import com.dungtran.codebase.ui.theme.Primary
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.launch

@Composable
fun LoginRoute(
    onLoginSuccess: () -> Unit,
    onGotoRegisterProfile: (String) -> Unit,
    onGotoSignup: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isLoginSuccess) {
        if (uiState.isLoginSuccess) {
            if (uiState.isExistUser) onLoginSuccess.invoke()
            else onGotoRegisterProfile.invoke(uiState.email)
        }
    }

    LoginScreen(
        uiState = uiState,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onRememberMeChange = viewModel::onRememberMeChange,
        onEmailLoginClick = viewModel::loginWithEmail,
        onGoogleLoginClick = viewModel::loginWithGoogle,
        onGotoSignup = onGotoSignup,
        modifier = modifier
    )
}

@Composable
fun LoginScreen(
    uiState: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRememberMeChange: (Boolean) -> Unit,
    onEmailLoginClick: () -> Unit,
    onGoogleLoginClick: (String) -> Unit,
    onGotoSignup: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.background_screen_type_three),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

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
            
            Image(
                painter = painterResource(id = R.drawable.icon_app),
                modifier = Modifier.size(100.dp),
                contentDescription = null,
                contentScale = ContentScale.Inside
            )

            Text(
                text = "Mixi Vivu",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(top = 8.dp)
            )

            Text(
                text = "App base made by Dũng Trần",
                style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray),
            )

            Text(
                text = "Login",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp),
                textAlign = TextAlign.Start
            )

            Text(
                text = "Enter your email and password to login",
                style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 8.dp),
                textAlign = TextAlign.Start
            )

            TextFieldWithIcon(
                value = uiState.email,
                onValueChange = onEmailChange,
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
                value = uiState.password,
                onValueChange = onPasswordChange,
                label = "Password",
                isPassword = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = Primary
                    )
                }
            )

            if (uiState.errorMessage != null) {
                Text(
                    text = uiState.errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Switch(
                        checked = uiState.isRememberMe,
                        onCheckedChange = onRememberMeChange,
                        modifier = Modifier.scale(0.8f),
                        colors = SwitchDefaults.colors(
                            checkedTrackColor = Primary
                        )
                    )
                    Text(
                        text = "Remember",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }

                TextButton(
                    onClick = { },
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = "Forgot password!",
                        color = Color.Red,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            LoadingButton(
                onClick = onEmailLoginClick,
                modifier = Modifier.fillMaxWidth(),
                isLoading = uiState.isLoading,
                enabled = !uiState.isLoading,
                text = "Login"
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Don't have an account? ",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )

                Row(
                    modifier = Modifier.clickable { onGotoSignup() },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Signup",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color(0xFF4A90E2)
                    )

                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier
                            .size(14.dp)
                            .padding(start = 1.dp),
                        tint = Color(0xFF4A90E2)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SocialButton(
                    text = "Google",
                    iconRes = R.drawable.ic_google,
                    onClick = handleGoogleLogin(onTokenReceived = onGoogleLoginClick),
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "or",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                SocialButton(
                    text = "Facebook",
                    iconRes = R.drawable.ic_facebook,
                    onClick = { /* Handle Login Facebook */ },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
fun handleGoogleLogin(
    onTokenReceived: (String) -> Unit
): () -> Unit {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val credentialManager = remember { CredentialManager.create(context) }
    val webClientId = stringResource(id = R.string.default_web_client_id)
    
    return remember(webClientId) {
        {
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(webClientId)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            scope.launch {
                try {
                    val result = credentialManager.getCredential(context, request)
                    val credential = result.credential

                    if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                        val idToken = googleIdTokenCredential.idToken
                        onTokenReceived(idToken)
                    } else {
                        // Handle error
                    }
                } catch (e: Exception) {
                    when (e) {
                        is NoCredentialException -> {
                            val intent = Intent(android.provider.Settings.ACTION_ADD_ACCOUNT).apply {
                                putExtra(android.provider.Settings.EXTRA_ACCOUNT_TYPES, arrayOf("com.google"))
                            }
                            context.startActivity(intent)
                        }
                        else -> {
                            // Handle error
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Màn hình Login đang Loading")
@Composable
fun LoginScreenLoadingPreview() {
    val mockUiState = LoginUiState(
        email = "user@example.com",
        password = "password123",
        isLoading = false,
        errorMessage = null,
        isLoginSuccess = false
    )

    LoginScreen(
        uiState = mockUiState,
        onEmailChange = {},
        onPasswordChange = {},
        onRememberMeChange = {},
        onEmailLoginClick = {},
        onGoogleLoginClick = {},
        onGotoSignup = {},
        modifier = Modifier
    )
}

/*
@Preview(showBackground = true, name = "Màn hình Login có lỗi")
@Composable
fun LoginScreenErrorPreview() {
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
        onRememberMeChange = {},
        onEmailLoginClick = {},
        modifier = Modifier
    )
}*/
