package com.dungtran.codebase.data.repository.firebase

import com.dungtran.codebase.domain.model.User
import com.dungtran.codebase.domain.repository.firebase.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : UserRepository {
    
    override fun getAllUsers(): Flow<Result<MutableList<User>>> = callbackFlow {
        val subscription = firestore.collection("users")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Result.failure(error))
                    return@addSnapshotListener
                }
                
                val users = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(User::class.java)?.copy(uid = doc.id)
                } ?: emptyList()
                trySend(Result.success(users.toMutableList()))
            }
        awaitClose { subscription.remove() }
    }

    override suspend fun getUserDetail(uid: String): Result<User> {
        TODO("Not yet implemented")
    }
}