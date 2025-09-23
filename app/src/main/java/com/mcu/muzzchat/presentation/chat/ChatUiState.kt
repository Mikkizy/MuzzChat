package com.mcu.muzzchat.presentation.chat

import com.mcu.muzzchat.domain.models.Message
import com.mcu.muzzchat.domain.models.MessageGroup
import com.mcu.muzzchat.domain.models.User

data class ChatUIState(
    val messages: List<MessageGroup> = emptyList(),
    val currentUser: User? = null,
    val otherUser: User? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)