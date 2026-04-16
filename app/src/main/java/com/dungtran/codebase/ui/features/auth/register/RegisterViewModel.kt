package com.dungtran.codebase.ui.features.auth.register

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dungtran.codebase.data.local.prefs.DataStoreManager
import com.dungtran.codebase.domain.usecase.firebase.AuthUseCase
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authUseCase: AuthUseCase, 
    private val dataStoreManager: DataStoreManager
) : ViewModel() {   
    private val _uiState = MutableStateFlow(RegisterUiState())
    
    val uiState = _uiState.asStateFlow()

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email, errorMessage = null) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password, errorMessage = null) }
    }

    fun onPasswordConfirmChange(passwordConfirm: String) {
        _uiState.update { it.copy(passwordConfirm = passwordConfirm, errorMessage = null) }
    }
    
    fun registerWithEmail() {
        val currentState = _uiState.value
        if (currentState.email.isBlank() || currentState.password.isBlank() || currentState.passwordConfirm.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Please enter all fields") }
            return
        }
        
        if (currentState.password != currentState.passwordConfirm) {
            _uiState.update { it.copy(errorMessage = "Password not match") }
            return
        }
        
        viewModelScope.launch { 
            _uiState.update { it.copy(isLoading = true) }
            
            authUseCase.signUpWithEmailUseCase(currentState.email, currentState.password)
                .onSuccess { 
                    Log.i("Atut", "Register success")
                    dataStoreManager.saveEmailRegister(currentState.email)
                    _uiState.update { it.copy(isLoading = false, isRegisterSuccess = true) }
                }
                .onFailure { e ->
                    Log.i("Atut", "Register error: ${e.localizedMessage}")
                    val errorMessage = when (e) {
                        is FirebaseAuthUserCollisionException -> "Email already in use"
                        is FirebaseAuthWeakPasswordException -> "Password should be at least 6 characters"
                        else -> e.localizedMessage ?: "Register failed"
                    }
                    
                    _uiState.update { it.copy(isLoading = false, errorMessage = errorMessage) }
                }
        }
    }
}