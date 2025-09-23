package com.mcu.muzzchat.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mcu.muzzchat.domain.models.Message
import java.time.LocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val text: String,
    val timestamp: String, // Stored as ISO string for Room compatibility
    val isFromCurrentUser: Boolean
)