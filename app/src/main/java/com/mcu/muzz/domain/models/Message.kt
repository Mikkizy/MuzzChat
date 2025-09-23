package com.mcu.muzz.domain.models

import java.time.LocalDateTime

data class Message(
    val id: String,
    val content: String,
    val senderId: String,
    val timestamp: LocalDateTime,
    val isRead: Boolean = false
)
