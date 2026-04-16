package com.dungtran.codebase.data.repository.firebase

import com.dungtran.codebase.data.local.prefs.DataStoreManager
import com.dungtran.codebase.domain.model.User
import com.dungtran.codebase.domain.repository.firebase.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val dataStoreManager: DataStoreManager,
    private val firestore: FirebaseFirestore
) : AuthRepository {
    override suspend fun signInWithEmail(email: String, password: String): Result<Boolean> {
        return try {
            val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            
            if (authResult.user == null) {
                return Result.failure(Exception("Login failed"))
            }

            dataStoreManager.saveAccessToken(authResult.user!!.uid)
            val isExistUser = checkExistUserProfile(authResult.user!!.uid).getOrNull() ?: false
            Result.success(isExistUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signInWithGoogle(idToken: String): Result<Unit> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val authResult = firebaseAuth.signInWithCredential(credential).await()

            authResult.user?.let {
                dataStoreManager.saveAccessToken(it.uid)
                checkAndCreateUserProfile(it)
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
                photoUrl = firebaseUser.photoUrl?.toString() ?: "",
                createdAt = System.currentTimeMillis()
            )
            userRef.set(newUser).await()
        }
    }

    override suspend fun signUpWithEmail(email: String, password: String): Result<Unit> {
        return try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            
            authResult.user?.let { firebaseUser ->
                dataStoreManager.saveAccessToken(firebaseUser.uid)
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createUserProfile(user: User): Result<Unit> {
        val idToken = dataStoreManager.accessToken.first()
        if (idToken.isNullOrEmpty()) {
            return Result.failure(Exception("User not logged in"))
        }
        return try {
            val userRef = firestore.collection("users").document(idToken)
            val document = userRef.get().await()
            if (!document.exists()) {
                val newUser = User(
                    uid = user.uid,
                    email = user.email,
                    displayName = user.displayName,
                    photoUrl = user.photoUrl
                )
                userRef.set(newUser).await()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun checkExistUserProfile(idToken: String): Result<Boolean> {
        return try {
            val userRef = firestore.collection("users").document(idToken)
            val document = userRef.get().await()
            Result.success(document.exists())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}