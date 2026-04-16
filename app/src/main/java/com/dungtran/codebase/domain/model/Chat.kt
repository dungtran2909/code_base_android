package com.dungtran.codebase.domain.model

import java.lang.reflect.Member

data class Chat(
    val roomId: String = "",
    val lastMessage: String = "",
    val lastTimestamp: Long = 0L,
    val members: List<String> = emptyList(),
    val roomName: String = "",
)