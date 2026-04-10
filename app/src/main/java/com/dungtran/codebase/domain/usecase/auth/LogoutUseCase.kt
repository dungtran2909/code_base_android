package com.dungtran.codebase.domain.usecase.auth

import com.dungtran.codebase.data.local.prefs.DataStoreManager
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val dataStoreManager: DataStoreManager
) {
    suspend operator fun invoke() {
        firebaseAuth.signOut()
        dataStoreManager.clearAccessToken()
    }
}