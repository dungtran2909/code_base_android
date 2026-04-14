package com.dungtran.codebase.domain.repository.firebase

import com.dungtran.codebase.domain.model.User

interface AuthRepository {
    suspend fun signInWithEmail(email: String, password: String): Result<Boolean>
    suspend fun signInWithGoogle(idToken: String): Result<Unit>
    suspend fun signUpWithEmail(email: String, password: String): Result<Unit>
    suspend fun createUserProfile(user: User): Result<Unit>
    suspend fun checkExistUserProfile(idToken: String): Result<Boolean>
}