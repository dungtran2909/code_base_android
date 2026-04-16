package com.dungtran.codebase.data.repository.firebase

import androidx.work.await
import com.dungtran.codebase.domain.model.Chat
import com.dungtran.codebase.domain.model.Message
import com.dungtran.codebase.domain.repository.firebase.ChatRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : ChatRepository {
    override suspend fun createRoom(user1Id: String, user2Id: String): Result<String> {
        return try {
            val roomId = getRoomId(user1Id, user2Id)
            val roomRef = firestore.collection("chat_rooms").document(roomId)

            // Dữ liệu cơ bản của phòng chat
            val roomData = mapOf(
                "roomId" to roomId,
                "members" to listOf(user1Id, user2Id),
                "lastMessage" to "Bắt đầu cuộc trò chuyện mới",
                "lastTimestamp" to System.currentTimeMillis()
            )
            
            roomRef.set(roomData, SetOptions.merge()).await()

            Result.success(roomId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun getRoomId(user1Id: String, user2Id: String): String {
        val ids = listOf(user1Id, user2Id).sorted()
        return "${ids[0]}_${ids[1]}"
    }

    override fun getMyChatRooms(myUid: String): Flow<Result<List<Chat>>> = callbackFlow {
        val subscription = firestore.collection("chat_rooms")
            .whereArrayContains("members", myUid)
            .orderBy("lastTimestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Result.failure(error))
                    return@addSnapshotListener
                }

                val chats = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Chat::class.java)
                } ?: emptyList()

                trySend(Result.success(chats))
            }
        awaitClose { subscription.remove() }
    }

    override suspend fun deleteRoom(roomId: String): Result<Unit> {
        return try {
            firestore.collection("chat_rooms")
                .document(roomId)
                .delete()
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getMessages(roomId: String): Flow<Result<List<Message>>> = callbackFlow {
        val subscription = firestore.collection("chat_rooms")
            .document(roomId)
            .collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Result.failure(error))
                    return@addSnapshotListener
                }
                val messages = snapshot?.documents?.mapNotNull { it.toObject(Message::class.java) } ?: emptyList()
                trySend(Result.success(messages))
            }
        awaitClose { subscription.remove() }
    }

    override suspend fun sendMessage(roomId: String, message: Message): Result<Unit> {
        return try {
            val roomRef = firestore.collection("chat_rooms").document(roomId)
            val messageRef = roomRef.collection("messages").document()
            val finalMessage = message.copy(messageId = messageRef.id)

            firestore.runBatch { batch ->
                batch.set(messageRef, finalMessage)
                batch.update(roomRef, "lastMessage", message.content)
                batch.update(roomRef, "lastTimestamp", message.timestamp)
            }.await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
}