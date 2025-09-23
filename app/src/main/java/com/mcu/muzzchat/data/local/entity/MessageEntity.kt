package com.mcu.muzzchat.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val text: String,
    val timestamp: String, // Stored as ISO string for Room compatibility
    val isFromCurrentUser: Boolean,
    val isRead: Boolean = false
)