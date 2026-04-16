package com.dungtran.codebase.domain.usecase.firebase

import com.dungtran.codebase.data.local.prefs.DataStoreManager
import com.dungtran.codebase.domain.model.User
import com.dungtran.codebase.domain.repository.firebase.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class AuthUseCase @Inject constructor(
    private val repository: AuthRepository,
    private val firebaseAuth: FirebaseAuth,
    private val dataStoreManager: DataStoreManager
) {
    suspend fun signInWithEmailUseCase(email: String, password: String) = repository.signInWithEmail(email, password)
    suspend fun signInWithGoogleUseCase(idToken: String) = repository.signInWithGoogle(idToken)
    suspend fun logoutUseCase() {
        firebaseAuth.signOut()
        dataStoreManager.clearAccessToken()
        dataStoreManager.clearEmailRegister()
    }
    suspend fun signUpWithEmailUseCase(email: String, password: String) = repository.signUpWithEmail(email, password)
    suspend fun createUserProfileUseCase(user: User) = repository.createUserProfile(user)
    suspend fun checkExistUserProfileUseCase(idToken: String) = repository.checkExistUserProfile(idToken)
}