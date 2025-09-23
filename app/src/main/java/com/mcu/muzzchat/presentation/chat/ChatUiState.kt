package com.mcu.muzzchat.presentation.chat

import com.mcu.muzzchat.domain.models.Message

data class ChatUiState(
    val messages: List<Message> = emptyList(),
    val currentMessage: String = "",
    val isLoading: Boolean = false
)