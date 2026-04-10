package com.dungtran.codebase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dungtran.codebase.data.local.prefs.DataStoreManager
import com.dungtran.codebase.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager
) : ViewModel() {
    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _startDestination = MutableStateFlow<Any>(Screen.Splash)
    val startDestination = _startDestination.asStateFlow()

    init {
        checkInitialState()
    }

    private fun checkInitialState() {
        viewModelScope.launch {
            delay(2000)
            val isFirstTime = dataStoreManager.isFirstTimeLaunchWelcome.first()
            val isExistToken = !dataStoreManager.accessToken.first().isNullOrEmpty()

            val destination = when {
                isFirstTime -> Screen.Welcome
                isExistToken -> Screen.MainContainer
                else -> Screen.Login
            }
            
            _startDestination.value = destination
            _isLoading.value = false
        }
    }
}