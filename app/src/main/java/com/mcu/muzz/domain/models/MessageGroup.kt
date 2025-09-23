package com.mcu.muzz.domain.models

data class MessageGroup(
    val messages: List<Message>,
    val timestamp: String? = null,
    val shouldShowTimestamp: Boolean = false
)