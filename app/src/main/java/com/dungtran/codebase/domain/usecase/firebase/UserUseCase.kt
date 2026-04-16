package com.dungtran.codebase.domain.usecase.firebase

import com.dungtran.codebase.domain.repository.firebase.UserRepository
import javax.inject.Inject

class UserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    fun getAllUsers() = userRepository.getAllUsers()
    
    suspend fun getUserDetail(uid: String) = userRepository.getUserDetail(uid)

}