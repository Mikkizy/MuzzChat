package com.mcu.muzz.presentation.chat

import com.mcu.muzz.domain.models.MessageGroup
import com.mcu.muzz.domain.models.User

data class ChatUIState(
    val messages: List<MessageGroup> = emptyList(),
    val currentUser: User? = null,
    val otherUser: User? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)