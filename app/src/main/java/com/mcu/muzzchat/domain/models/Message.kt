package com.mcu.muzzchat.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

data class Message(
    val id: Long = 0,
    val text: String,
    val timestamp: LocalDateTime,
    val isFromCurrentUser: Boolean,
    val isRead: Boolean = false
)
