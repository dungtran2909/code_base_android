package com.dungtran.codebase.ui.features.main.chat.private_chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dungtran.codebase.data.local.prefs.DataStoreManager
import com.dungtran.codebase.domain.model.Message
import com.dungtran.codebase.domain.usecase.firebase.AuthUseCase
import com.dungtran.codebase.domain.usecase.firebase.ChatUseCase
import com.dungtran.codebase.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrivateChatViewModel @Inject constructor(
    private val chatUseCase: ChatUseCase, 
    private val dataStoreManager: DataStoreManager,
    private val authUseCase: AuthUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(PrivateChatUiState())
    val uiState = _uiState.asStateFlow()
    private val _roomId = MutableStateFlow<String?>(null)
    
    fun initData(dataScreen: Screen.PrivateChat) {
        _uiState.update { it.copy(dataScreen = dataScreen) }
        _roomId.value = dataScreen.roomId
    }
    
    init {
        viewModelScope.launch {
            val uid = dataStoreManager.accessToken.first()
            _uiState.update { it.copy(myUid = uid ?: "") }
            _roomId.filterNotNull().collect { roomId ->
                observeMessages(roomId)
            }
        }    
    }
    
    private fun observeMessages(roomId: String = "") {
        viewModelScope.launch { 
            _uiState.update { it.copy(isLoading = true) }
            
            /*_uiState.map { it.dataScreen?.roomId }.filterNotNull().collect { roomId ->
                chatUseCase.getMessages(roomId).collect { result ->
                    result.onSuccess { msgList ->
                        _uiState.update {
                            it.copy(messageList = msgList, isLoading = false)
                        }
                    }.onFailure { error ->
                        _uiState.update {
                            it.copy(errorMessage = error.message, isLoading = false)
                        }
                    }
                }
            }*/
            chatUseCase.getMessages(roomId).collect { result ->
                result.onSuccess { msgList ->
                    _uiState.update {
                        it.copy(messageList = msgList, isLoading = false)
                    }
                }.onFailure { error ->
                    _uiState.update {
                        it.copy(errorMessage = error.message, isLoading = false)
                    }
                }
            }
        }
    }
    
    fun sendMessage(content: String) {
        if (content.isBlank()) return
        val roomId = _uiState.value.dataScreen?.roomId ?: return
        
        viewModelScope.launch {
            val newMessage = Message(
                senderId = _uiState.value.myUid,
                content = content.trim(),
                timestamp = System.currentTimeMillis()
            )
            
            chatUseCase.sendMessage(roomId, newMessage)
        }
    }
}