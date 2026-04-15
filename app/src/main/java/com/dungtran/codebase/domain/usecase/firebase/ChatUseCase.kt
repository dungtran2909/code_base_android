package com.dungtran.codebase.domain.usecase.firebase

import com.dungtran.codebase.domain.model.Message
import com.dungtran.codebase.domain.repository.firebase.ChatRepository
import javax.inject.Inject

class ChatUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend fun createRoom(user1Id: String, user2Id: String): Result<String> = repository.createRoom(user1Id, user2Id)
    fun getMyChatRooms(myUid: String) = repository.getMyChatRooms(myUid)
    suspend fun deleteRoom(roomId: String) = repository.deleteRoom(roomId)
    fun getMessages(roomId: String) = repository.getMessages(roomId)
    suspend fun sendMessage(roomId: String, message: Message) = repository.sendMessage(roomId, message)
}
