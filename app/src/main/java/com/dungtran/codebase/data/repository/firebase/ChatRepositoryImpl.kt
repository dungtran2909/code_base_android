package com.dungtran.codebase.data.repository.firebase

import com.dungtran.codebase.domain.repository.firebase.ChatRepository
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : ChatRepository {

}