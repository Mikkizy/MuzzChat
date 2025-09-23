package com.mcu.muzzchat.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

data class Message(
    val id: String,
    val content: String,
    val senderId: String,
    val timestamp: LocalDateTime,
    val isRead: Boolean = false
)
