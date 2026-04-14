package com.dungtran.codebase.ui.features.auth.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dungtran.codebase.data.local.prefs.DataStoreManager
import com.dungtran.codebase.domain.usecase.firebase.AuthUseCase
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val authUseCase: AuthUseCase
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

    fun loginWithEmail() {
        val currentState = _uiState.value
        if (currentState.email.isBlank() || currentState.password.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Please enter all fields") }
            return
        }

        viewModelScope.launch {
            dataStoreManager.saveCredentials(
                email = currentState.email,
                password = currentState.password,
                isRemember = currentState.isRememberMe
            )
            
            _uiState.update { it.copy(isLoading = true) }
            
            authUseCase.signInWithEmailUseCase(currentState.email, currentState.password)
                .onSuccess { isExistUser ->
                    Log.i("Atut", "success")
                    dataStoreManager.saveEmailRegister(currentState.email)
                    _uiState.update { it.copy(isLoading = false, isLoginSuccess = true, isExistUser = isExistUser) }
                }
            .onFailure { e ->
                    Log.i("Atut", "error: ${e.localizedMessage}")
                    val errorMessage = when (e) {
                        is FirebaseAuthInvalidUserException -> "Email or Password is not correct"
                        is FirebaseAuthInvalidCredentialsException -> "Password is not correct"
                        else -> e.localizedMessage ?: "Login failed"
                    }
                    _uiState.update { it.copy(isLoading = false, errorMessage = errorMessage) }
                }
        }
    }
    
    fun loginWithGoogle(idToken: String) {
        viewModelScope.launch { 
            _uiState.update { it.copy(isLoading = true) }
            
            authUseCase.signInWithGoogleUseCase(idToken)
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false, isLoginSuccess = true) }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
                }
        }
    }
}