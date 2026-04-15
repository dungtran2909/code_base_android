package com.dungtran.codebase.ui.features.main.chat.private_chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dungtran.codebase.domain.usecase.firebase.ChatUseCase
import com.dungtran.codebase.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrivateChatViewModel @Inject constructor(
    private val chatUseCase: ChatUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(PrivateChatUiState())
    val uiState = _uiState.asStateFlow() 
    
    fun initData(dataScreen: Screen.PrivateChat) {
        _uiState.update { it.copy(dataScreen = dataScreen) }
    }
    
    init {
        viewModelScope.launch { 
            
        }    
    }
    
}