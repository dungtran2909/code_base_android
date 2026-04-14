package com.dungtran.codebase.ui.features.main.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dungtran.codebase.data.local.prefs.DataStoreManager
import com.dungtran.codebase.domain.model.User
import com.dungtran.codebase.domain.usecase.firebase.AuthUseCase
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
    private val authUseCase: AuthUseCase
) : ViewModel() {   
    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadUsers()
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
}
