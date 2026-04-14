package com.dungtran.codebase.ui.features.auth.register_profile

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.dungtran.codebase.data.local.prefs.DataStoreManager
import com.dungtran.codebase.domain.model.User
import com.dungtran.codebase.domain.usecase.firebase.AuthUseCase
import com.dungtran.codebase.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterProfileViewModel @Inject constructor(
    private val authUseCase: AuthUseCase, 
    private val dataStoreManager: DataStoreManager,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val registerArgs = savedStateHandle.toRoute<Screen.RegisterProfile>()
    private val _uiState = MutableStateFlow(RegisterProfileUiState())
    val uiState = _uiState.asStateFlow()
    
    init {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(email = registerArgs.email)
        }
    }
    
    fun onDisplayNameChange(displayName: String) {
        _uiState.value = _uiState.value.copy(displayName = displayName)
    }
    
    fun createUserProfile() {
        val currentState = _uiState.value
        if (currentState.displayName.isBlank()) {
            _uiState.value = _uiState.value.copy(errorMessage = "Please enter your display name")
            return
        }
        
        viewModelScope.launch { 
            val accessToken = dataStoreManager.accessToken.first()
            if (accessToken.isNullOrEmpty()) {
                _uiState.value = _uiState.value.copy(errorMessage = "User not logged in")
                return@launch
            }
            val newUser = User(
                uid = accessToken,
                displayName = currentState.displayName, 
                email = currentState.email, 
                photoUrl = "https://images2.thanhnien.vn/528068263637045248/2025/9/22/1-1758546255166427717171.jpg"
            )
            _uiState.value = _uiState.value.copy(isLoading = true)
            authUseCase.createUserProfileUseCase(user = newUser)
                .onSuccess { 
                    Log.i("Atut", "Register profile success")
                    _uiState.value = _uiState.value.copy(isLoading = false, isRegisterProfileSuccess = true)
                }
                .onFailure { e ->
                    Log.i("Atut", "Register profile error: ${e.localizedMessage}")
                    _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = e.localizedMessage ?: "An error occurred")
                }   
        }
    }
    
    fun onCancelRegisterNewUser() {
        viewModelScope.launch {
            dataStoreManager.clearAccessToken()
            dataStoreManager.clearEmailRegister()
        }
    }
}