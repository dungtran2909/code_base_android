package com.dungtran.codebase.ui.features.main.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dungtran.codebase.data.local.prefs.DataStoreManager
import com.dungtran.codebase.domain.usecase.firebase.AuthUseCase
import com.dungtran.codebase.domain.usecase.firebase.UserUseCase
import com.dungtran.codebase.ui.features.main.chat.private_chat.PrivateChatUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authUseCase: AuthUseCase, 
    private val userUseCase: UserUseCase,
    private val dataStoreManager: DataStoreManager,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()
    private val _isLogoutSuccess = MutableStateFlow(false)
    val isLogoutSuccess = _isLogoutSuccess.asStateFlow()
    
    init {
        viewModelScope.launch {
            val uid = dataStoreManager.accessToken.first()
            _uiState.update { it.copy(myUid = uid ?: "") }
            fetchUserProfile()
        }
    }

    fun logout() {
        viewModelScope.launch {
            authUseCase.logoutUseCase()
            _isLogoutSuccess.value = true
        }
    }
    
    suspend fun fetchUserProfile() {
        _uiState.update { it.copy(isLoading = true) }
        userUseCase.getUserDetail(_uiState.value.myUid)
            .onSuccess { user -> 
                _uiState.update { it.copy(isLoading = false, userProfile = user) }
            }
            .onFailure { e ->
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
    }
}