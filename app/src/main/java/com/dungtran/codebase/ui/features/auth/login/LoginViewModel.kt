package com.dungtran.codebase.ui.features.auth.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dungtran.codebase.data.local.prefs.DataStoreManager
import com.dungtran.codebase.domain.usecase.SignInWithGoogleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            // Lấy trạng thái "Ghi nhớ" từ DataStore
            val isRemembered = dataStoreManager.isRemembered.first()
            if (isRemembered) {
                val email = dataStoreManager.savedEmail.first()
                val password = dataStoreManager.savedPassword.first()
                _uiState.update {
                    it.copy(
                        email = email,
                        password = password,
                        isRememberMe = true
                    )
                }
            }
        }
    }

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email, errorMessage = null) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password, errorMessage = null) }
    }

    fun onRememberMeChange(checked: Boolean) {
        _uiState.update { it.copy(isRememberMe = checked) }
    }

    fun login() {
        val currentState = _uiState.value
        if (currentState.email.isBlank() || currentState.password.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Vui lòng nhập đầy đủ thông tin") }
            return
        }

        viewModelScope.launch {
            dataStoreManager.saveCredentials(
                email = currentState.email,
                password = currentState.password,
                isRemember = currentState.isRememberMe
            )
            
            _uiState.update { it.copy(isLoading = true) }

            // Giả lập gọi API login
            delay(2000)

            if (currentState.email == "admin" && currentState.password == "123456") {
                _uiState.update { it.copy(isLoading = false, isLoginSuccess = true) }
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Email hoặc mật khẩu sai"
                    )
                }
            }
        }
    }
    
    fun loginWithGoogle(idToken: String) {
        viewModelScope.launch { 
            _uiState.update { it.copy(isLoading = true) }
            signInWithGoogleUseCase(idToken)
                .onSuccess {
                    Log.i("Atut", "Login success")
                    _uiState.update { it.copy(isLoading = false, isLoginSuccess = true) }
                }
                .onFailure { e ->
                    Log.i("Atut", "Login onFailure: ${e.message}")
                    _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
                }
        }
    }
}