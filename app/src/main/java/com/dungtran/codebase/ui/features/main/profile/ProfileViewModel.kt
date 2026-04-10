package com.dungtran.codebase.ui.features.main.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dungtran.codebase.domain.usecase.auth.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _isLogoutSuccess = MutableStateFlow(false)
    val isLogoutSuccess = _isLogoutSuccess.asStateFlow()

    fun logout() {
        viewModelScope.launch {
            logoutUseCase()
            _isLogoutSuccess.value = true
        }
    }
}