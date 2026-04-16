package com.dungtran.codebase.domain.model

data class User(
    val uid: String = "",
    val email: String = "",
    val displayName: String = "",
    val photoUrl: String = "",
    val createdAt: Long = 0L, 
    val isMe: Boolean = false, 
    val thinking: String = ""
)