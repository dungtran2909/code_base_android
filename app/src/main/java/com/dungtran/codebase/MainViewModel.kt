package com.dungtran.codebase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dungtran.codebase.data.local.prefs.PreferenceManager
import com.dungtran.codebase.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val preferenceManager: PreferenceManager
) : ViewModel() {
    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _startDestination = MutableStateFlow<Any>(Screen.Splash)
    val startDestination = _startDestination.asStateFlow()

    init {
        viewModelScope.launch {
            delay(2000)
            val destination = if (preferenceManager.isFirstTimeLaunchWelcome()) {
                Screen.Welcome
            } else {
                Screen.Login
            }
            _startDestination.value = destination
            _isLoading.value = false
        }
    }
}