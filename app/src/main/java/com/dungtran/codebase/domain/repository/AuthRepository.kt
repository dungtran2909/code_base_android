package com.dungtran.codebase.domain.repository

interface AuthRepository {
    suspend fun signInWithEmail(email: String, password: String): Result<Unit>
    suspend fun signInWithGoogle(idToken: String): Result<Unit> 
}