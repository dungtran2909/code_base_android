package com.dungtran.codebase.domain.repository.firebase

import com.dungtran.codebase.domain.model.Chat
import com.dungtran.codebase.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun createRoom(user1Id: String, user2Id: String): Result<String>
    fun getMyChatRooms(myUid: String): Flow<Result<List<Chat>>>
    suspend fun deleteRoom(roomId: String): Result<Unit>
    fun getMessages(roomId: String): Flow<Result<List<Message>>>
    suspend fun sendMessage(roomId: String, message: Message): Result<Unit>
}