package com.dungtran.codebase.domain.usecase.firebase

import com.dungtran.codebase.domain.repository.firebase.ChatRepository
import javax.inject.Inject

class ChatUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    
}
