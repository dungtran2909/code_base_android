package com.dungtran.codebase.ui.features.main.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dungtran.codebase.data.local.prefs.DataStoreManager
import com.dungtran.codebase.domain.model.User
import com.dungtran.codebase.domain.usecase.firebase.AuthUseCase
import com.dungtran.codebase.domain.usecase.firebase.ChatUseCase
import com.dungtran.codebase.domain.usecase.firebase.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
    private val dataStoreManager: DataStoreManager,
    private val chatUseCase: ChatUseCase
) : ViewModel() {   
    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val uid = dataStoreManager.accessToken.first() ?: ""
            _uiState.update { it.copy(myUid = uid) }

            loadUsers()
            observeChatRooms()   
        }
    }
    
    fun loadUsers() {
        viewModelScope.launch { 
            _uiState.update { it.copy(isLoading = true) }
            userUseCase.getAllUsers().collect { result ->
                result.onSuccess { allUsers ->
                    val processedUsers = updateUserList(allUsers)
                    _uiState.update {
                        it.copy(
                            users = processedUsers,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                }
                result.onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.localizedMessage
                        )
                    }
                }
            }
        }
    }
    
    suspend fun updateUserList(allUsers: MutableList<User>): MutableList<User> {
        val myUid = dataStoreManager.accessToken.first()
        return allUsers.map { user ->
            user.copy(isMe = user.uid == myUid)
        }.sortedByDescending { it.isMe }.toMutableList()
    }
    
    fun getRoomId(myId: String, partnerId: String): String {
        return if (myId < partnerId) "${myId}_${partnerId}" else "${partnerId}_${myId}"
    }
    
    fun gotoPrivateChat(partnerId: String) {
        viewModelScope.launch {
            createRoom(dataStoreManager.accessToken.first() ?: "", partnerId)
        }
    }

    
    fun createRoom(user1Id: String, user2Id: String) {
        if (user1Id == user2Id) {
            return
        }
        if (user1Id.isBlank() || user2Id.isBlank()) {
            _uiState.update { it.copy(errorMessage = "User IDs cannot be empty") }
            return
        }
        viewModelScope.launch {
            chatUseCase.createRoom(user1Id, user2Id).onSuccess {

            }.onFailure { 
                
            }
        }
    }

    private fun observeChatRooms() {
        viewModelScope.launch {
            val myUid = dataStoreManager.accessToken.first() ?: return@launch

            chatUseCase.getMyChatRooms(myUid).collect { result ->
                result.onSuccess { chatList ->
                    _uiState.update { it.copy(chats = chatList) }
                }
                result.onFailure { error ->
                    Log.e("ChatViewModel", "Error fetching rooms: ${error.message}")
                }
            }
        }
    }
}
