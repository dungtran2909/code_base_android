package com.dungtran.codebase.domain.repository.firebase

import com.dungtran.codebase.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getAllUsers(): Flow<Result<MutableList<User>>>

    suspend fun getUserDetail(uid: String): Result<User>
}