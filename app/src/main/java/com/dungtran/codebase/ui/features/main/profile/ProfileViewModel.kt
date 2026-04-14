package com.dungtran.codebase.ui.features.main.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dungtran.codebase.domain.usecase.firebase.AuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authUseCase: AuthUseCase
) : ViewModel() {

    private val _isLogoutSuccess = MutableStateFlow(false)
    val isLogoutSuccess = _isLogoutSuccess.asStateFlow()

    fun logout() {
        viewModelScope.launch {
            authUseCase.logoutUseCase()
            _isLogoutSuccess.value = true
        }
    }
}