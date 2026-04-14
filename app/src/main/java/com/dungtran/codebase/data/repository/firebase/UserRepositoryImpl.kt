package com.dungtran.codebase.data.repository.firebase

import com.dungtran.codebase.domain.repository.firebase.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : UserRepository {
    
}