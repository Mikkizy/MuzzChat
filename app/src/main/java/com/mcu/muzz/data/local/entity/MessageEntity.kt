package com.mcu.muzz.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "chats")
data class MessageEntity(
    @PrimaryKey val id: String,
    val content: String,
    val senderId: String,
    val timestamp: LocalDateTime,
    val isRead: Boolean = false
)