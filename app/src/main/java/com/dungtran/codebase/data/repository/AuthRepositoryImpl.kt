package com.dungtran.codebase.data.repository

import com.dungtran.codebase.data.local.prefs.DataStoreManager
import com.dungtran.codebase.domain.model.User
import com.dungtran.codebase.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val dataStoreManager: DataStoreManager,
    private val firestore: FirebaseFirestore
) : AuthRepository {
    override suspend fun signInWithEmail(email: String, password: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun signInWithGoogle(idToken: String): Result<Unit> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val authResult = firebaseAuth.signInWithCredential(credential).await()

            authResult.user?.let { 
                checkAndCreateUserProfile(it)
                dataStoreManager.saveAccessToken(it.uid)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun checkAndCreateUserProfile(firebaseUser: FirebaseUser) {
        val userRef = firestore.collection("users").document(firebaseUser.uid)
        val document = userRef.get().await()

        // Nếu chưa có Profile (User mới) thì mới tạo
        if (!document.exists()) {
            val newUser = User(
                uid = firebaseUser.uid,
                email = firebaseUser.email ?: "",
                displayName = firebaseUser.displayName ?: "",
                photoUrl = firebaseUser.photoUrl?.toString() ?: ""
            )
            userRef.set(newUser).await()
        }
    }
}