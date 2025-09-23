package com.mcu.muzzchat.domain.models

import java.time.LocalDateTime

data class Message(
    val id: Long = 0,
    val text: String,
    val timestamp: LocalDateTime,
    val isFromCurrentUser: Boolean,
    val isRead: Boolean = false
)
